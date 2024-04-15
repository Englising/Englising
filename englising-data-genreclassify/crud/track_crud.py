from typing import List

from database.mysql_manager import Session
from model.track import Track


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result


def get_tracks_without_genre(session) -> List[Track]:
    return (session.query(Track)
            .filter(Track.genre == None)
            .limit(300)
            .all())


def update_track_genre(session: Session, track: Track, genre: str):
    session.query(Track).filter(Track.track_id == track.track_id).update({"genre": genre})
    session.flush()