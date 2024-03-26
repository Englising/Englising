from typing import List

from dto.track_dto import YoutubeQueryDto
from model.artist import Artist
from model.lyric import Lyric
from model.track import Track


def create_track(track: Track, session):
    session.add(track)
    session.flush()
    session.refresh(track)
    return track


def update_track(youtube: YoutubeQueryDto, session):
    session.query(Track).filter(Track.track_id == youtube.track_id).update({"youtube_id": youtube.youtube_id})
    session.flush()


def get_track_by_spotify_id(spotify_id: str, session):
    result = session.query(Track).filter(Track.spotify_id == spotify_id).one_or_none()
    return result


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result


def get_youtube_id_unfigured_tracks(session) -> List[YoutubeQueryDto]:
    tracks = session.query(Track).filter(Track.youtube_id == None).limit(20)
    tracks_with_top_artist = []
    for track in tracks:
        top_artist: Artist = None
        max_popularity = -1
        if track.artists is None or len(track.artists) == 0:
            continue
        for artist in track.artists:
            if artist.spotify_popularity is not None and artist.spotify_popularity > max_popularity:
                top_artist = artist
                max_popularity = artist.spotify_popularity
        if top_artist:
            tracks_with_top_artist.append(YoutubeQueryDto(
                track_id=track.track_id,
                title=track.title,
                duration_ms=track.duration_ms,
                artist_name=top_artist.name
            ))
    return tracks_with_top_artist


def get_tracks_without_lyrics(session) -> Track:
    return session.query(Track) \
        .outerjoin(Lyric, Track.track_id == Lyric.track_id) \
        .filter(Lyric.lyric_id == None) \
        .all()