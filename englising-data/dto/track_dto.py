from typing import List, Optional
from pydantic import BaseModel


class TrackDto(BaseModel):
    track_id: Optional[int] = None
    track_index: Optional[int] = None
    title: Optional[str] = None
    spotify_id: Optional[str] = None
    youtube_id: Optional[str] = None
    isrc: Optional[str] = None
    spotify_popularity: Optional[int] = None
    duration_ms: Optional[int] = None
    is_lyrics: Optional[bool] = None
    feature_acousticness: Optional[float] = None
    feature_danceability: Optional[float] = None
    feature_energy: Optional[float] = None
    feature_positiveness: Optional[float] = None
    artists: Optional[List[str]] = None


class YoutubeDto(BaseModel):
    youtube_id: Optional[str] = None
    duration_ms: Optional[int] = None


class MusixMatchDto(BaseModel):
    album: str = "",
    artist: str = "",
    track_name: str = "",
    track_spotify_id: str = "",
    track_duration: int = 0