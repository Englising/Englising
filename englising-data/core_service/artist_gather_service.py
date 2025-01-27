import time
from queue import Queue, Empty

import redis

from database.mysql_manager import Session
from model import album
from util.worklist import WorkList
from client.spotify_client import *
from crud.album_crud import *
from crud.artist_crud import *
from crud.artist_album_crud import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

# 변경 사항
# 1. Redis Job Queue가 특정 개수 이하일 경우, DB의 Ablum 중 ArtistAlbum에 없는 앨범 정보 조회, Job Queue에 넣음
# 2. Spotify에서 해당 앨범의 상세 정보 조회
# 3. Spotify에서 해당 Artist들 상세 정보 조회
# 4. Artist, Artist_Album DB에 저장


class ArtistWorker:
    def __init__(self):
        self.job_queue = Queue()

    def start(self):
        while True:
            session = Session()
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                albums_without_artists = get_album_without_artist(session)
                for album in albums_without_artists:
                    self.job_queue.put(album)
            session.close()
            try:
                album_dto = self.job_queue.get(timeout=5)
                self.process_job(album_dto)
                log(LogList.ARTIST.name, LogKind.INFO, "Finished Job: " + str(album_dto))
                time.sleep(10)
            except Empty:
                time.sleep(10)

    def process_job(self, album_dto: Album):
        log(LogList.ARTIST.name, LogKind.INFO, "Starting Job: "+str(album_dto.album_id))
        session = Session()
        try:
            artist_spotify_ids = get_artists_by_album_spotify_id(album_dto.spotify_id)
            for artist_spotify_id in artist_spotify_ids:
                artist = get_artist_by_spotify_id(artist_spotify_id, session)
                if artist is None:
                    artist: ArtistDto = get_artist_by_spotify_id_spotify(artist_spotify_id)
                    artist = create_artist(Artist(
                        name=artist.name,
                        genres=artist.genres,
                        spotify_id=artist.spotify_id,
                        spotify_popularity=artist.spotify_popularity,
                        image=artist.image
                    ), session)
                if get_artist_album_by_artist_id_album_id(artist.artist_id, album_dto.album_id, session) is None:
                    create_artist_album(ArtistAlbum(
                        artist_id=artist.artist_id,
                        album_id=album_dto.album_id
                    ), session)
            session.commit()
        except ArtistException as e:
            log(LogList.ARTIST.name, LogKind.ERROR, str(e))
            self.job_queue.put(album_dto)
            session.rollback()
            time.sleep(30)
        except Exception as e:
            log(LogList.ARTIST.name, LogKind.ERROR, str(e))
            self.job_queue.put(album_dto)
            session.rollback()
            time.sleep(5)
        finally:
            session.close()

