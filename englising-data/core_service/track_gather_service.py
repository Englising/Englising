import time
from queue import Queue, Empty

import redis

from database.mysql_manager import Session
from client.spotify_client import *
from client.youtube_client import *
from crud.album_crud import *
from crud.track_crud import *
from crud.artist_track_crud import *
from crud.artist_crud import *
from client.google_trans_client import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

# 변경 사항
# 1. Redis Job Queue가 특정 개수 이하일 경우, DB에서 Track이 없는 앨범 정보 조회, Job Queue에 넣음
# 2. Spotify에서 해당 앨범의 상세 정보 조회
# 3. Spotify에서 해당 앨범의 트랙들 상세 정보 조회
# 4. Track DB에 저장

class TrackWorker:

    def __init__(self):
        self.job_queue = Queue()

    # def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.TRACK.name):
    #     self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
    #     self.queue_name = queue_name

    def start(self):
        while True:
            session = Session()
            # queue_length = self.redis_connection.llen(self.queue_name)
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                albums_without_tracks = get_album_without_tracks(session)
                for album in albums_without_tracks:
                    self.job_queue.put(album)
                    # self.redis_connection.rpush(self.queue_name, album.json())
            session.close()

            # _, album_dto_json = self.redis_connection.blpop(self.queue_name, timeout=None)
            # album_dto = Album.parse_raw(album_dto_json)
            try:
                album_dto = self.job_queue.get(timeout=5)
                self.process_job(album_dto)
                log(LogList.TRACK.name, LogKind.INFO, "Finished Job: " + str(album_dto))
                time.sleep(5)
            except Empty:
                time.sleep(10)

    def process_job(self, album_dto):
        log(LogList.TRACK.name, LogKind.INFO, "Starting Job: "+str(album_dto))
        session = Session()
        try:
            track_spotify_ids = get_tracks_by_album_spotify_id(album_dto.spotify_id)
            print(track_spotify_ids)
            for track_spotify_id in track_spotify_ids:
                track = get_track_by_spotify_id(track_spotify_id, session)
                if track is None:
                    track: TrackDto = get_track_by_spotify_id_spotify(track_spotify_id)
                    artists = track.artists
                    track = get_track_audiofeature(track_spotify_id, track)
                    if track is not None and detect_lyric_language(track.title):
                        track_entity = create_track(Track(
                            album_id=album_dto.album_id,
                            track_index=track.track_index,
                            title=track.title,
                            spotify_id=track.spotify_id,
                            youtube_id=track.youtube_id,
                            isrc=track.isrc,
                            spotify_popularity=track.spotify_popularity,
                            duration_ms=track.duration_ms,
                            feature_acousticness=track.feature_acousticness,
                            feature_danceability=track.feature_danceability,
                            feature_energy=track.feature_energy,
                            feature_positiveness=track.feature_positiveness
                        ), session)
                        for artist in artists:
                            artist_entity = get_artist_by_spotify_id(artist, session)
                            if artist_entity is not None:
                                create_artist_track(ArtistTrack(
                                    artist_id=get_artist_by_spotify_id(artist, session).artist_id,
                                    track_id=track_entity.track_id
                                ), session)
                        session.commit()
        except TrackException as e:
            log(LogList.TRACK.name, LogKind.ERROR, str(e))
            self.job_queue.put(album_dto)
            # self.redis_connection.rpush(self.queue_name, album_dto.json())
            session.rollback()
            time.sleep(30)
        except Exception as e:
            log(LogList.TRACK.name, LogKind.ERROR, str(e))
            self.job_queue.put(album_dto)
            session.rollback()
            time.sleep(30)
        finally:
            session.close()
