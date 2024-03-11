from model.artist_album import ArtistAlbum


def create_artist_album(artist_album: ArtistAlbum, session):
    session.add(artist_album)
    session.flush()
    session.refresh(artist_album)
    return artist_album


def get_artist_album_by_album_id(album_id: int, session):
    return session.query(ArtistAlbum).filter(ArtistAlbum.album_id == album_id).all()
