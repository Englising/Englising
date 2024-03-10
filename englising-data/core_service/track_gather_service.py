import time
import redis

from util.worklist import WorkList
from client.spotify_client import *
from client.youtube_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

MAX_RETRY = 5


class TrackWorker:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.TRACK.name):
        self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
        self.queue_name = queue_name

    def start(self):
        while True:
            _, job_dto_json = self.redis_connection.blpop(self.queue_name, timeout=None)
            job_dto = JobDto.parse_raw(job_dto_json)
            self.process_job(job_dto)
            time.sleep(10)

    def process_job(self, job_dto):
        log(LogList.TRACK.name, LogKind.INFO, "Starting Job: "+str(job_dto))
        try:
            # Album에서 가져온 Track의 spotify_id로 정보 가져오기
            job_dto.tracks = []
            for track_id in job_dto.track_ids:
                # Spotify Track 정보 가져오기
                track = get_track_by_spotify_id(track_id)
                # Spotify Audiofeatures 정보 가져오기
                if track.feature_acousticness is None:
                    track_audio = get_track_audiofeature(track_id)
                    if track_audio is None:
                        log(LogList.TRACK.name, LogKind.WARNING,"AudioFeature is not present, skipping track "+str(track_id))
                        self.remove_job(track, track_id, job_dto)
                        return
                    else:
                        track = self.get_audio(track, track_audio)
                # Youtube Id 정보 가져오기
                if track.youtube_id is None:
                    closest_youtube = search_youtube(track.title, self.figure_artist(track, job_dto.artists).name, track.duration_ms)
                    if closest_youtube is None:
                        log(LogList.TRACK.name, LogKind.WARNING,"Close youtube video is not present, skipping track "+str(track_id))
                        self.remove_job(track, track_id, job_dto)
                        return
                    else:
                        track.youtube_id = closest_youtube.youtube_id
                job_dto.tracks.append(track)
            self.redis_connection.rpush(WorkList.LYRICS.name, job_dto.json())
        except TrackException as e:
            log(LogList.TRACK.name, LogKind.ERROR, str(e))
            self.retry_job(job_dto)

    def retry_job(self, job_dto):
        if job_dto.retry < MAX_RETRY:
            job_dto.retry += 1
            log(LogList.TRACK.name, LogKind.WARNING, f"Retrying job: {job_dto}. Attempt #{job_dto.retry}")
            self.redis_connection.rpush(self.queue_name, job_dto.json())
        else:
            log(LogList.TRACK.name, LogKind.ERROR, f"Job failed after {MAX_RETRY} retries: {job_dto}")

    def remove_job(self, track:TrackDto, track_id: str, job_dto):
        job_dto.tracks.remove(track)
        job_dto.track_ids.remove(track_id)

    def get_audio(self, track:TrackDto, track_audio:TrackDto):
        track.feature_acousticness = track_audio.feature_acousticness
        track.feature_danceability = track_audio.feature_danceability
        track.feature_energy = track_audio.feature_energy
        track.feature_positiveness = track_audio.feature_positiveness
        return track

    def figure_artist(self, track:TrackDto, job_dto:JobDto):
        for artist in job_dto.artists:
            if artist.spotify_id == track.artists[0]:
                return artist