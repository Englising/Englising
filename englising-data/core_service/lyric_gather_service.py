import time
import redis

from util.worklist import WorkList
from client.spotify_client import *
from client.musix_client import *
from client.papago_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

MAX_RETRY = 5


class LyricWorker:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.LYRICS.name):
        self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
        self.queue_name = queue_name

    def start(self):
        while True:
            _, job_dto_json = self.redis_connection.blpop(self.queue_name, timeout=None)
            job_dto = JobDto.parse_raw(job_dto_json)
            self.process_job(job_dto)
            time.sleep(10)

    def process_job(self, job_dto):
        log(LogList.LYRICS.name, LogKind.INFO, "Starting Job: "+str(job_dto))
        try:
            for track in job_dto.tracks:
                # 가사 가져오기
                if track.lyrics is None or len(track.lyrics) == 0:
                    track.lyrics = find_lyrics(MusixMatchDto(
                        album=job_dto.album.title,
                        artist=self.figure_artist(track, job_dto).name,
                        track_name=track.title,
                        track_spotify_id=track.spotify_id,
                        track_duration=track.duration_ms
                    ))
                # 가사 영어인지 확인하기
                if not detect_lyric_language(track.lyrics[0].en_text) :
                    log(LogList.LYRICS.name, LogKind.INFO, "Lyric is not english " + track.title)
                    self.remove_job(track.spotify_id, job_dto)
                    return
                # 가사 해석본 가져오기
                for lyric in track.lyrics:
                    if lyric.kr_text is None:
                        lyric.kr_text = get_lyric_translation(lyric.en_text, track.spotify_id)
            job_dto.retry = 0
            print(job_dto)
            self.redis_connection.rpush(WorkList.SAVE.name, job_dto.json())
        except LyricException as e:
            log(LogList.LYRICS.name, LogKind.ERROR, str(e))
            if "DROP" in e.args:
                self.remove_job(e.track_spotify_id, job_dto)
                return
            elif "TIMEOUT" in e.args:
                self.retry_job(job_dto)
                time.sleep(300)

    def retry_job(self, job_dto):
        if job_dto.retry < MAX_RETRY:
            job_dto.retry += 1
            log(LogList.LYRICS.name, LogKind.WARNING, f"Retrying job: {job_dto}. Attempt #{job_dto.retry}")
            self.redis_connection.rpush(self.queue_name, job_dto.json())
        else:
            log(LogList.LYRICS.name, LogKind.ERROR, f"Job failed after {MAX_RETRY} retries: {job_dto}")

    def remove_job(self, track_spotify_id: str, job_dto):
        for track in job_dto.tracks:
            if track.spotify_id == track_spotify_id:
                job_dto.tracks.remove(track)
        job_dto.track_ids.remove(track_spotify_id)

    def figure_artist(self, track:TrackDto, job_dto:JobDto):
        for artist in job_dto.artists:
            if artist.spotify_id == track.artists[0]:
                return artist