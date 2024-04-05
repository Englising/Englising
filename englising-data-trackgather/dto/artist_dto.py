from typing import List, Optional
from pydantic import BaseModel


class ArtistDto(BaseModel):
    artist_id: Optional[int] = None
    name: Optional[str] = None
    genres: Optional[str] = None
    spotify_id: Optional[str] = None
    spotify_popularity: Optional[int] = None
    image: Optional[str] = None
