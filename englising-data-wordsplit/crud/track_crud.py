from typing import List

from database.mysql_manager import Session
from model.track import Track
from model.track_word import TrackWord


def get_tracks_without_words(session: Session) -> List[Track]:
    return (session.query(Track)
            .outerjoin(TrackWord, Track.track_id == TrackWord.track_id)
            .filter(TrackWord.track_id.is_(None))
            .filter(Track.lyric_status.is_(None))
            .limit(1000)
            .all())


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result


def update_track_lyric_status(track_id: int, status: str, session):
    session.query(Track).filter(Track.track_id == track_id).update({"lyric_status": status})
    session.flush()
