from typing import List

from sqlalchemy import desc, func

from database.mysql_manager import Session
from model import Track
from model.single_play import Singleplay
from model.track_like import TrackLike


def get_all_tracks(session: Session) -> List[Track]:
    return session.query(Track.title, Track.track_id, Track.spotify_id, Track.spotify_popularity, Track.feature_acousticness, Track.feature_danceability, Track.feature_energy) \
        .join(Track.track_words) \
        .filter(Track.youtube_id != None) \
        .filter(Track.genre != None) \
        .filter(Track.lyric_status == 'DONE') \
        .distinct().all()


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
              .order_by(desc(Track.spotify_popularity), desc(singleplay_count_subquery.c.play_count)).all())

    return tracks[:limit]