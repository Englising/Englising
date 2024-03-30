from model.album import Album


def get_album_by_album_id(album_id: int, session):
    result = session.query(Album).filter(Album.album_id == album_id).one_or_none()
    return result
