from model.album import Album
from model.artist_album import ArtistAlbum
from model.track import Track


def get_album_without_tracks(session):
    artist_albums = session.query(ArtistAlbum.album_id).join(Album, Album.album_id == ArtistAlbum.album_id).subquery()
    albums_without_tracks = session.query(Album).join(
        artist_albums, Album.album_id == artist_albums.c.album_id
    ).outerjoin(
        Track, Album.album_id == Track.album_id
    ).filter(
        Track.album_id == None
    ).group_by(Album.album_id).limit(300)
    return albums_without_tracks
