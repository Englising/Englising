import time
import redis

from util.worklist import WorkList
from client.spotify_client import *
from client.youtube_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

MAX_RETRY = 3


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

            self.redis_connection.rpush(WorkList.LYRICS.name, job_dto.json())
        except TrackException as e:
            log(LogList.TRACK.name, LogKind.ERROR, str(e))
            self.retry_job(job_dto)

    def retry_job(self, job_dto):
        if job_dto.retry < MAX_RETRY:
            job_dto.retry += 1
            log(LogList.LYRICS.name, LogKind.WARNING, f"Retrying job: {job_dto}. Attempt #{job_dto.retry}")
            self.redis_connection.rpush(self.queue_name, job_dto.json())
        else:
            log(LogList.LYRICS.name, LogKind.ERROR, f"Job failed after {MAX_RETRY} retries: {job_dto}")

    def remove_job(self, track:TrackDto, track_id: str, job_dto):
        job_dto.tracks.remove(track)
        job_dto.track_ids.remove(track_id)
