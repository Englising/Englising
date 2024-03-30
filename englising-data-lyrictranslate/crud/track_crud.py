from model.track import Track


def create_track(track: Track, session):
    session.add(track)
    session.flush()
    session.refresh(track)
    return track


def update_track(lyric_status: str, track_id: int, session):
    session.query(Track).filter(Track.track_id == track_id).update({"lyric_status": lyric_status})
    session.flush()


def get_track_by_track_id(track_id: int, session):
    return session.query(Track).filter(Track.track_id == track_id).one_or_none()
