
from model.artist_track import ArtistTrack


def create_artist_track(artist_track: ArtistTrack, session):
    session.add(artist_track)
    session.flush()
    session.refresh(artist_track)
    return artist_track
