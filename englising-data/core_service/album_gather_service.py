import time
import redis

from util.worklist import WorkList
from client.spotify_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log


class AlbumWorker:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.ALBUM.name):
        self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
        self.queue_name = queue_name

    def start(self):
        while True:
            job_left = self.redis_connection.llen(WorkList.TRACK.name)
            if job_left <= 10:
                _, year = self.redis_connection.blpop(self.queue_name, timeout=None)
                self.process_job(year)
            time.sleep(300)

    def process_job(self, year):
        log(LogList.ALBUM.name, LogKind.INFO, "Starting Job: "+str(year))
        try:
            album_ids = get_albums_by_year(year)
            jobs = []
            for album_id in album_ids:
                jobs.append(get_album_by_spotify_id(album_id))
                time.sleep(1)
            for job in jobs:
                self.redis_connection.rpush(WorkList.ARTIST.name, job.json())
            self.redis_connection.rpush(WorkList.ALBUM.name, year-1)
        except AlbumException as e:
            log(LogList.ALBUM.name, LogKind.ERROR, str(e))
            self.redis_connection.rpush(WorkList.ALBUM.name, year)

