from typing import List, Optional
from pydantic import BaseModel

from dto.album_dto import AlbumDto
from dto.artist_dto import ArtistDto
from dto.track_dto import TrackDto


class ArtistJobDto(BaseModel):
    album: AlbumDto = None
    artists: List[str] = None
    tracks: List[str] = None


class TrackJobDto(BaseModel):
    album: AlbumDto = None
    artist: List[int] = None
    track: str = None


