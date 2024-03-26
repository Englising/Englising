import time
from queue import Queue, Empty

import redis

from database.mysql_manager import Session
from util.worklist import WorkList
from client.google_trans_client import *
from client.musix_client import *
from crud.album_crud import *
from crud.track_crud import *
from crud.artist_track_crud import *
from crud.lyric_crud import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

# 변경 사항
# 1. Redis Job Queue가 특정 개수 이하일 경우, DB에서 Lyric이 없는 Track 정보 조회, Job Queue에 넣음
# 2. MusixMatch에서 Lyric 조회
# 3. Google에서 Lyric 번역 조회
# 4. Lyric DB에 저장

class LyricWorker:
    def __init__(self):
        self.job_queue = Queue()

    def start(self):
        while True:
            session = Session()
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                tracks_without_lyrics = get_tracks_without_lyrics(session)
                for track in tracks_without_lyrics:
                    self.job_queue.put(track)
            session.close()

            try:
                track_dto = self.job_queue.get(timeout=5)
                self.process_job(track_dto)
                log(LogList.LYRICS.name, LogKind.INFO, "Finished Job: " + str(track_dto))
                time.sleep(10)
            except Empty:
                time.sleep(10)

    def process_job(self, track_dto):
        log(LogList.LYRICS.name, LogKind.INFO, "Starting Job: "+str(track_dto))
        session = Session()
        try:
            artist = get_most_popular_artist_by_track_id(track_dto.track_id, session)
            album = get_album_by_album_id(track_dto.album_id, session)
            lyrics: List[LyricDto] = find_lyrics(MusixMatchDto(
                album=album.title,
                artist=artist.name,
                track_name=track_dto.title,
                track_spotify_id=track_dto.spotify_id,
                track_duration=track_dto.duration_ms
            ))
            # 가사 영어인지 확인하기
            if not detect_lyric_language(lyrics[0].en_text):
                log(LogList.LYRICS.name, LogKind.INFO, "Lyric is not english " + track_dto.title)
                return
            # 가사 해석본 가져오기
            for lyric in lyrics:
                if lyric.kr_text is None:
                    lyric.kr_text = get_translation(lyric.en_text)
            for lyric in lyrics:
                create_lyric(Lyric(
                    track_id=track_dto.track_id,
                    start_time=lyric.start_time,
                    end_time=lyric.end_time,
                    en_text=lyric.en_text,
                    kr_text=lyric.kr_text
                ), session)
            session.commit()
        except LyricException as e:
            log(LogList.LYRICS.name, LogKind.ERROR, str(e))
            session.rollback()
            if "TIMEOUT" in e.args:
                self.job_queue.put(track_dto)
                time.sleep(180)
        except GoogleException as e:
            self.job_queue.put(track_dto)
            time.sleep(30)
        except Exception as e:
            log(LogList.LYRICS.name, LogKind.ERROR, str(e))
            self.job_queue.put(track_dto)
            session.rollback()
            time.sleep(30)
        finally:
            session.close()
