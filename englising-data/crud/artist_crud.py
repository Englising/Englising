from model.artist import Artist


def create_artist(artist: Artist, session):
    session.add(artist)
    session.flush()
    session.refresh(artist)
    return artist


def get_artist_by_spotify_id(spotify_id: str, session):
    result = session.flush().query(Artist).filter(Artist.spotify_id == spotify_id).one_or_none()
    return result


def get_artist_by_artist_id(artist_id: int, session):
    result = session.query(Artist).filter(Artist.artist_id == artist_id).one_or_none()
    return result

