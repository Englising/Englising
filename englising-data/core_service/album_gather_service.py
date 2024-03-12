import time
import redis

from database.mysql_manager import Session
from client.spotify_client import *
from crud.album_crud import *
from client.google_trans_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

# 변경 사항
# 1. 5분마다 년도별 앨범 정보 수집
# 2. 해당 앨범이 DB에 저장되어 있는지 확인 후 저장 되어있지 않을 경우 저장

class AlbumWorker:
    def start(self):
        year = 2024
        while year > 1950:
            self.process_job(year)
            log(LogList.ALBUM.name, LogKind.INFO, "Finished Job: " + str(year))
            year -= 1
            time.sleep(10)

    def process_job(self, year):
        log(LogList.ALBUM.name, LogKind.INFO, "Starting Job: " + str(year))
        session = Session()
        try:
            albums: List[AlbumDto] = get_albums_by_year(year)
            for album in albums:
                if detect_lyric_language(album.title):
                    album_db = get_album_by_spotify_id(album.spotify_id, session)
                    if album_db is None and get_album_popularity_by_spotify_id(album.spotify_id) >= 80:
                        album_db = create_album(Album(
                            title=album.title,
                            type=album.type,
                            total_tracks=album.total_tracks,
                            spotify_id=album.spotify_id,
                            cover_image=album.cover_image,
                            release_date=album.release_date,
                        ), session)
                        session.commit()
        except AlbumException as e:
            log(LogList.ALBUM.name, LogKind.ERROR, str(e))
            session.rollback()
            time.sleep(30)
        except GoogleException as e:
            log(LogList.ALBUM.name, LogKind.ERROR, str(e))
            session.rollback()
            time.sleep(30)
        except Exception as e:
            log(LogList.ALBUM.name, LogKind.ERROR, str(e))
            session.rollback()
            time.sleep(30)
        finally:
            session.close()
