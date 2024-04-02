from typing import List, Optional
from pydantic import BaseModel

from dto.album_dto import AlbumDto
from dto.artist_dto import ArtistDto
from dto.lyric_dto import LyricDto
from dto.track_dto import TrackDto


class TrackAlbumArtistDto(BaseModel):
    track: Optional[TrackDto] = None
    album: Optional[AlbumDto] = None
    artist: Optional[ArtistDto] = None
    lyrics: Optional[List[LyricDto]] = None