import time
import redis

from dto.job_dto import JobDto
from util.worklist import WorkList
from database.mysql_manager import Session
from crud.album_crud import *
from crud.artist_crud import *
from crud.track_crud import *
from crud.lyric_crud import *
from crud.artist_track_crud import *
from crud.artist_album_crud import *

from log.log_info import LogList, LogKind
from log.englising_logger import log

MAX_RETRY = 3


class SaveWorker:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.SAVE.name):
        self.redis_connection = redis.Redis(host=redis_host, port=redis_port, db=redis_db)
        self.queue_name = queue_name

    def start(self):
        while True:
            _, job_dto_json = self.redis_connection.blpop(self.queue_name, timeout=None)
            job_dto = JobDto.parse_raw(job_dto_json)
            self.process_job(job_dto)
            time.sleep(10)

    def process_job(self, job_dto):
        log(LogList.SAVE.name, LogKind.INFO, "Starting Job: "+str(job_dto))
        print("SAVING ---------------------------------------------------------------------------")
        session = Session()
        try:
            # Save Album
            album = get_album_by_spotify_id(job_dto.album.spotify_id, session)
            if album is None :
                album = create_album(Album(
                    title=job_dto.album.title,
                    type=job_dto.album.type,
                    total_tracks=job_dto.album.total_tracks,
                    spotify_id=job_dto.album.spotify_id,
                    cover_image=job_dto.album.cover_image,
                    release_date=job_dto.album.release_date,
                ), session)
            job_dto.album = album
            # Save Artist
            for artist in job_dto.artists:
                artist_db = get_artist_by_spotify_id(artist.spotify_id, session)
                if artist_db is None :
                    artist_db = create_artist(Artist(
                        name=artist.name,
                        genres=artist.genres,
                        spotify_id=artist.spotify_id,
                        spotify_popularity=artist.spotify_popularity,
                        image=artist.image
                    ), session)
                artist = artist_db
            # Save Track
            for track in job_dto.tracks:
                track_db = get_track_by_spotify_id(track, session)
                if track_db is None :
                    track_db = create_track(Track(
                        album_id=album.album_id,
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
                track = track_db
            # Save Lyrics
            for track in job_dto.tracks:
                for lyric in track.lyrics:
                    lyric = create_lyric(Lyric(
                        track_id=track.track_id,
                        start_time=lyric.start_time,
                        end_time=lyric.end_time,
                        en_text=lyric.en_text,
                        kr_text=lyric.kr_text
                    ), session)
            # Save Artist-Album
            for artist in job_dto.artists:
                create_artist_album(ArtistAlbum(
                    album_id=job_dto.album.album_id,
                    artist_id=artist
                ), session)
            # Save Artist-Track
            for track in job_dto.tracks:
                for artist_spotify_id in track.artists:
                    artist = self.check_artist(artist_spotify_id, job_dto.artists)
                    if artist is not None:
                        create_artist_track(ArtistTrack(
                            artist_id=artist.artist_id,
                            track_id=track.track_id
                        ), session)
            session.commit()
        except Exception as e:
            log(LogList.ARTIST.name, LogKind.ERROR, str(e))
            session.rollback()
            self.retry_job(job_dto)
        finally:
            session.close()

    def retry_job(self, job_dto):
        if job_dto.retry < MAX_RETRY:
            job_dto.retry += 1
            log(LogList.ARTIST.name, LogKind.WARNING, f"Retrying job: {job_dto}. Attempt #{job_dto.retry}")
            self.redis_connection.rpush(self.queue_name, job_dto.json())
        else:
            log(LogList.ARTIST.name, LogKind.ERROR, f"Job failed after {MAX_RETRY} retries: {job_dto}")

    def check_artist(self, spotify_id, artists):
        for artist in artists:
            if artist.spotify_id == spotify_id:
                return artist
