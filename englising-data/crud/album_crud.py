from model.album import Album


def create_album(album: Album, session):
    session.add(album)
    session.flush()
    session.refresh(album)
    return album


def get_album_by_spotify_id(spotify_id: str, session):
    result = session.flush().query(Album).filter(Album.spotify_id == spotify_id).one_or_none()
    return result


def get_album_by_album_id(album_id: int, session):
    result = session.query(Album).filter(Album.album_id == album_id).one_or_none()
    return result