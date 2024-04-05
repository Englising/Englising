import os
import pickle
import time
from typing import List
import redis
from dotenv import load_dotenv
from sqlalchemy import desc, func, and_

from database.mysql_manager import Session
from model import Track, Lyric
from model.single_play import Singleplay
from model.track_like import TrackLike


load_dotenv()

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", 6379))
REDIS_DB = int(os.getenv("REDIS_DB", 0))
r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)


def get_all_tracks(session: Session) -> List[Track]:
    cache_key = "Recommend:GetAllTracks"
    cached_tracks = r.get(cache_key)

    if cached_tracks is not None:
        return pickle.loads(cached_tracks)
    else:
        tracks = session.query(Track) \
            .join(Track.track_words) \
            .filter(Track.youtube_id is not None and Track.youtube_id != 'NONE') \
            .filter(Track.genre is not None) \
            .filter(Track.lyric_status == 'DONE') \
            .filter(Track.lyrics.any(Lyric.kr_text is not None)) \
            .all()
        r.set(cache_key, pickle.dumps(tracks), ex=43200)
        return tracks


def get_user_liked_track_ids(session, user_id, limit=5):
    liked_tracks = session.query(TrackLike.track_id).filter(TrackLike.user_id == user_id, TrackLike.is_liked == True).order_by(desc(TrackLike.created_at)).limit(limit).all()
    track_ids = [track_like[0] for track_like in liked_tracks]
    return track_ids


def get_user_played_tracks(session, user_id):
    played_tracks = session.query(Track).join(Singleplay, Track.track_id == Singleplay.track_id).filter(Singleplay.user_id == user_id).all()
    return played_tracks


def get_tracks_by_single_played_count_and_spotify_popularity(session, limit=60):
    singleplay_count_subquery = session.query(
        Singleplay.track_id,
        func.count(Singleplay.singleplay_id).label('play_count')
    ).group_by(Singleplay.track_id).subquery()

    tracks = (session.query(Track)
              .outerjoin(singleplay_count_subquery, Track.track_id == singleplay_count_subquery.c.track_id)
              .filter(and_(Track.youtube_id != None, Track.youtube_id != 'NONE'))
              .filter(Track.genre != None)
              .filter(Track.lyric_status == 'DONE')
              .filter(Track.lyrics.any(Lyric.kr_text != None))
              .order_by(desc(Track.spotify_popularity), desc(singleplay_count_subquery.c.play_count))
              .limit(limit)
              .all())

    return tracks


def measure_execution_time():
    start_time = time.time()
    session = Session()
    tracks = get_all_tracks(session)
    end_time = time.time()
    execution_time = end_time - start_time
    print(f"Execution time: {execution_time} seconds")
