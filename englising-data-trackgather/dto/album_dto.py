from datetime import datetime
from typing import Optional
from pydantic import BaseModel


class AlbumDto(BaseModel):
    album_id: Optional[int] = None
    title: Optional[str] = None
    type: Optional[str] = None
    total_tracks: Optional[int] = None
    spotify_id: Optional[str] = None
    cover_image: Optional[str] = None
    release_date: Optional[datetime] = None