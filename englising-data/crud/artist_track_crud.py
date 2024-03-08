from model.artist_track import ArtistTrack


def create_artist_track(artist_track: ArtistTrack, session):
    session.add(artist_track)
    session.flush()
    session.refresh(artist_track)
    return artist_track


def get_artist_track_by_artist_id(artist_id: int, session):
    return session.query(ArtistTrack).filter(ArtistTrack.artist_id == artist_id).all()
