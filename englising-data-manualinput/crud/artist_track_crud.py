from sqlalchemy import func

from model.artist import Artist
from model.artist_track import ArtistTrack


def create_artist_track(artist_track: ArtistTrack, session):
    session.add(artist_track)
    session.flush()
    session.refresh(artist_track)
    return artist_track


def get_artist_track_by_artist_id(artist_id: int, session):
    return session.query(ArtistTrack).filter(ArtistTrack.artist_id == artist_id).all()


def get_most_popular_artist_by_track_id(track_id: int, session):
    artist_subquery = session.query(
        ArtistTrack.track_id,
        Artist.artist_id,
        Artist.name,
        Artist.spotify_popularity
    ).join(
        Artist, Artist.artist_id == ArtistTrack.artist_id
    ).filter(
        ArtistTrack.track_id == track_id
    ).subquery()

    top_artist = session.query(
        artist_subquery.c.artist_id,
        artist_subquery.c.name,
        func.max(artist_subquery.c.spotify_popularity).label('max_popularity')
    ).group_by(
        artist_subquery.c.artist_id,
        artist_subquery.c.name
    ).order_by(
        func.max(artist_subquery.c.spotify_popularity).desc()
    ).first()
    return top_artist
