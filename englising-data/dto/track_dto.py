from typing import List, Optional
from pydantic import BaseModel


class TrackDto(BaseModel):
    track_id: Optional[int]
    track_index: Optional[int]
    title: Optional[str]
    spotify_id: Optional[str]
    youtube_id: Optional[str]
    isrc: Optional[str]
    spotify_popularity: Optional[int]
    duration_ms: Optional[int]
    is_lyrics: Optional[bool]
    feature_acousticness: Optional[float]
    feature_danceability: Optional[float]
    feature_energy: Optional[float]
    feature_positiveness: Optional[float]
