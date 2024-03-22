import time
import redis

from util.worklist import WorkList
from client.spotify_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

MAX_RETRY = 3


class ArtistWorker:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.ARTIST.name):
        self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
        self.queue_name = queue_name

    def start(self):
        while True:
            _, job_dto_json = self.redis_connection.blpop(self.queue_name, timeout=None)
            job_dto = JobDto.parse_raw(job_dto_json)
            self.process_job(job_dto)
            time.sleep(10)

    def process_job(self, job_dto):
        log(LogList.ARTIST.name, LogKind.INFO, "Starting Job: "+str(job_dto))
        try:
            # 1. Album에서 가져온 Artist의 spotify_id로 존재 유무 확인, 정보 가져오기
            job_dto.artists = []
            for artist_id in job_dto.artist_ids:
                job_dto.artists.append(get_artist_by_spotify_id(artist_id))
            job_dto.retry = 0
            self.redis_connection.rpush(WorkList.TRACK.name, job_dto.json())
        except ArtistException as e:
            log(LogList.ARTIST.name, LogKind.ERROR, str(e))
            self.retry_job(job_dto)

    def retry_job(self, job_dto):
        if job_dto.retry < MAX_RETRY:
            job_dto.retry += 1
            log(LogList.ARTIST.name, LogKind.WARNING, f"Retrying job: {job_dto}. Attempt #{job_dto.retry}")
            self.redis_connection.rpush(self.queue_name, job_dto.json())
        else:
            log(LogList.ARTIST.name, LogKind.ERROR, f"Job failed after {MAX_RETRY} retries: {job_dto}")

    def check_artist(self, spotify_id, artists) -> bool:
        for artist in artists:
            if artist.spotify_id == spotify_id:
                return True
        return False
