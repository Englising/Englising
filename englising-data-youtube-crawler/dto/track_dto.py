from typing import List, Optional
from pydantic import BaseModel


class YoutubeDto(BaseModel):
    youtube_id: Optional[str] = None
    duration_ms: Optional[int] = None


class YoutubeQueryDto(BaseModel):
    track_id: Optional[int] = None
    title: Optional[str] = None
    duration_ms: Optional[int] = None
    youtube_id: Optional[str] = None
    artist_name: Optional[str] = None
