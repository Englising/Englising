import time
from queue import Queue, Empty
from typing import List

from client.easy_google_trans import EasyGoogleTranslate
from database.mysql_manager import Session
import crud.lyric_crud as lyric_crud
import crud.track_crud as track_crud
from log.englising_logger import log
from log.log_info import LogList, LogKind
import client.google_trans_client as google_trans_client
from model.lyric import Lyric


# kr_text가 없는 Lyric의 track_id를 조회
# track_id 단위로 List[Lyric]을 가져옴
# Lyric 번역 입력 후 Lyric의 kr_text 업데이트


class LyricTranslateWorker:
    def __init__(self):
        self.job_queue = Queue()
        self.easy_google_trans = EasyGoogleTranslate(source_language='en', target_language='ko')

    def start(self):
        while True:
            session = Session()
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                track_ids = lyric_crud.get_track_ids_without_kr_lyric(session)
                for track_id_tuple in track_ids:
                    track_id = track_id_tuple[0]
                    self.job_queue.put(lyric_crud.get_lyrics_by_track_id(session, track_id))
            session.close()
            try:
                lyrics: List[Lyric] = self.job_queue.get(timeout=5)
                self.process_job(lyrics)
                log(LogList.TRANSLATE.name, LogKind.INFO, "Finished Job: " + str(lyrics))
                time.sleep(5)
            except Empty:
                time.sleep(5)

    def process_job(self, lyrics: List[Lyric]):
        log(LogList.TRANSLATE.name, LogKind.INFO, "Starting Job: " + str(lyrics))
        session = Session()
        try:
            for lyric in lyrics:
                if not google_trans_client.detect_lyric_language(lyric.en_text):
                    track_crud.update_track("NOTENGLISH", lyric.en_text)
                    return
                lyric.kr_text = self.easy_google_trans.translate(lyric.en_text)
                lyric_crud.update_lyric_kr_text(lyric, session)
            session.commit()
        except Exception as e:
            log(LogList.TRANSLATE.name, LogKind.ERROR, str(e))
            self.job_queue.put(lyrics)
            session.rollback()
        finally:
            session.close()