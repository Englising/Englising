from typing import List

from model.lyric import Lyric
from model.track import Track
from model.track_word import TrackWord


def get_tracks_without_words(session) -> List[Track]:
    return (session.query(Track)
            .outerjoin(TrackWord, Track.track_id == TrackWord.track_id)
            .filter(TrackWord.track_id == None)
            .filter(Track.lyric_status != 'RATEDR')
            .limit(100)
            .all())


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result


def update_track_lyric_status(track_id: int, status: str, session):
    session.query(Track).filter(Track.track_id == track_id).update({"lyric_status": status})
    session.flush()
