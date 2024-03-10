from typing import List, Optional
from pydantic import BaseModel

from dto.album_dto import AlbumDto
from dto.artist_dto import ArtistDto
from dto.lyric_dto import LyricDto
from dto.track_dto import TrackDto


class JobDto(BaseModel):
    album: AlbumDto = None
    artist_ids: List[str] = None
    track_ids: List[str] = None
    artists: List[ArtistDto] = None
    tracks: List[TrackDto] = None
    lyrics: List[LyricDto] = None
    retry: int = 0
