
from model.track import Track


def create_track(track: Track, session):
    session.add(track)
    session.flush()
    session.refresh(track)
    return track


def get_track_by_spotify_id(spotify_id: str, session):
    result = session.query(Track).filter(Track.spotify_id == spotify_id).one_or_none()
    return result


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result
