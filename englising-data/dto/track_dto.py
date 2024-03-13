from typing import List, Optional
from pydantic import BaseModel

from dto.lyric_dto import LyricDto


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
    lyrics: Optional[List[LyricDto]] = None


class YoutubeDto(BaseModel):
    youtube_id: Optional[str] = None
    duration_ms: Optional[int] = None


class MusixMatchDto(BaseModel):
    album: str = "",
    artist: str = "",
    track_name: str = "",
    track_spotify_id: str = "",
    track_duration: int = 0


class YoutubeQueryDto(BaseModel):
    track_id: Optional[int] = None
    title: Optional[str] = None
    duration_ms: Optional[int] = None
    youtube_id: Optional[str] = None
    artist_name: Optional[str] = None