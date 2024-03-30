from typing import List

from dto.track_dto import YoutubeQueryDto
from model.track import Track
from model.artist import Artist


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result


def update_track_youtube_status(youtube: YoutubeQueryDto, session):
    session.query(Track).filter(Track.track_id == youtube.track_id).update({"youtube_id": youtube.youtube_id})
    session.flush()


def get_youtube_id_unfigured_tracks(session) -> List[YoutubeQueryDto]:
    tracks = session.query(Track).filter(Track.youtube_id == None).limit(100)
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
