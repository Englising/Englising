from model.album import Album
from model.artist_album import ArtistAlbum
from model.track import Track


def create_album(album: Album, session):
    session.add(album)
    session.flush()
    session.refresh(album)
    return album


def get_album_by_spotify_id(spotify_id: str, session):
    result = session.query(Album).filter(Album.spotify_id == spotify_id).one_or_none()
    return result


def get_album_by_album_id(album_id: int, session):
    result = session.query(Album).filter(Album.album_id == album_id).one_or_none()
    return result


def get_album_without_artist(session):
    query = session.query(Album).outerjoin(ArtistAlbum, Album.album_id == ArtistAlbum.album_id).filter(
        ArtistAlbum.album_id == None)
    return query.all()


def get_album_without_tracks(session):
    artist_albums = session.query(ArtistAlbum.album_id).join(Album, Album.album_id == ArtistAlbum.album_id).subquery()
    albums_without_tracks = session.query(Album).join(
        artist_albums, Album.album_id == artist_albums.c.album_id
    ).outerjoin(
        Track, Album.album_id == Track.album_id
    ).filter(
        Track.album_id == None
    ).group_by(Album.album_id).all()
    return albums_without_tracks
