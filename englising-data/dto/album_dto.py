from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel


class AlbumDto(BaseModel):
    album_id: Optional[int]
    artists: Optional[List[str]]
    title: Optional[str]
    type: Optional[str]
    total_tracks: Optional[int]
    spotify_id: Optional[str]
    cover_image: Optional[str]
    release_date: Optional[datetime]