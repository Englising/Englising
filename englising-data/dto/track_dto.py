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
    artists = Optional[List[str]] = None