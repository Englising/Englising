import time
from queue import Queue

import redis

from database.mysql_manager import Session
from log.englising_logger import log
from log.log_info import LogList, LogKind
from util.custom_exception import YoutubeException
from util.worklist import WorkList

from client.youtube_client import *
from crud.track_crud import *

# 변경 사항
# 1. Redis Job Queue가 특정 개수 이하일 경우, DB에서 Track 중 youtube_id가 없는 트랙 조회, Job Queue에 넣음
# 2. Youtube 해당 트랙의 정보 조회
# 3. 해당하는 영상이 있을 경우, youtube_id update
# 4. 해당하는 영상이 없을 경우, NONE으로 update

class YoutubeWorker:

    def __init__(self):
        self.job_queue = Queue()
    # def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.YOUTUBE.name):
    #     self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
    #     self.queue_name = queue_name

    def start(self):
        while True:
            session = Session()
            # queue_length = self.redis_connection.llen(self.queue_name)
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                tracks_without_youtube_id = get_youtube_id_unfigured_tracks(session)
                for track in tracks_without_youtube_id:
                    self.job_queue.put(track)
                    # self.redis_connection.rpush(WorkList.YOUTUBE.name, track.json())
            session.close()

            # _, youtube_query_json = self.redis_connection.blpop(self.queue_name, timeout=None)
            # youtube_query_dto = YoutubeQueryDto.parse_raw(youtube_query_json)
            youtube_query_dto = self.job_queue.get(timeout=5)
            self.process_job(youtube_query_dto)
            log(LogList.YOUTUBE.name, LogKind.INFO, "Finished Job: " + str(youtube_query_dto))
            time.sleep(10)

    def process_job(self, youtube_query_dto):
        log(LogList.YOUTUBE.name, LogKind.INFO, "Starting Job: " + str(youtube_query_dto))
        session = Session()
        try:
            youtube_result = search_youtube(
                youtube_query_dto.title,
                youtube_query_dto.artist_name,
                youtube_query_dto.duration_ms
            )
            youtube_query_dto.youtube_id = "NONE" if youtube_result.youtube_id is None else youtube_result.youtube_id
            update_track(youtube_query_dto, session)
            session.commit()
        except YoutubeException as e:
            log(LogList.YOUTUBE.name, LogKind.ERROR, str(e))
            self.job_queue.put(youtube_query_dto)
            # self.redis_connection.rpush(self.queue_name, youtube_query_dto.json())
            session.rollback()
            time.sleep(30)
        except Exception as e:
            log(LogList.YOUTUBE.name, LogKind.ERROR, str(e))
            self.job_queue.put(youtube_query_dto)
            session.rollback()
            time.sleep(30)
        finally:
            session.close()
