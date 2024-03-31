from model.artist import Artist


def get_artist_by_spotify_id(spotify_id: str, session):
    result = session.query(Artist).filter(Artist.spotify_id == spotify_id).one_or_none()
    return result
