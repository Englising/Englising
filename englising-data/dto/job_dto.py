from typing import List, Optional
from pydantic import BaseModel

from dto.album_dto import AlbumDto
from dto.artist_dto import ArtistDto
from dto.track_dto import TrackDto


class ArtistJobDto(BaseModel):
    album: AlbumDto
    artists: List[str]
    tracks: List[str]


class TrackJobDto(BaseModel):
    album: AlbumDto
    artist: List[int]
    track: str

