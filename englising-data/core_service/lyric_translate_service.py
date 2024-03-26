import time
from queue import Queue, Empty

from database.mysql_manager import Session
from crud.lyric_crud import *
from log.englising_logger import log
from log.log_info import LogList, LogKind
from client.google_trans_client import *


# 변경 사항
# 1. Redis Job Queue가 특정 개수 이하일 경우, DB에서 kr_text이 없는 Lyric 정보 조회, Job Queue에 넣음
# 2. GoogleTranslate에서 Lyric 조회
# 4. Lyric DB 업데이트

class LyricTranslateWorker:
    def __init__(self):
        self.job_queue = Queue()

    def start(self):
        while True:
            session = Session()
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                lyrics_without_kr = get_lyrics_without_kr(session)
                for lyric in lyrics_without_kr:
                    self.job_queue.put(lyric)
            session.close()
            try:
                lyric_dto = self.job_queue.get(timeout=5)
                self.process_job(lyric_dto)
                log(LogList.TRANSLATE.name, LogKind.INFO, "Finished Job: " + str(lyric_dto))
                time.sleep(10)
            except Empty:
                time.sleep(10)

    def process_job(self, lyric_dto):
        log(LogList.TRANSLATE.name, LogKind.INFO, "Starting Job: " + str(lyric_dto))
        session = Session()
        try:
            # 가사 해석본 가져오기
            kr_text = get_translation(lyric_dto.en_text)
            if kr_text is not None:
                lyric_dto.kr_text = kr_text
                update_lyric_kr_text(lyric_dto, session)
                session.commit()
        except GoogleException as e:
            log(LogList.TRANSLATE.name, LogKind.ERROR, str(e))
            session.rollback()
            self.job_queue.put(lyric_dto)
            time.sleep(30)
        except Exception as e:
            log(LogList.TRANSLATE.name, LogKind.ERROR, str(e))
            self.job_queue.put(lyric_dto)
            session.rollback()
            time.sleep(30)
        finally:
            session.close()
