from typing import List, Optional
from pydantic import BaseModel


class ArtistDto(BaseModel):
    artist_id: Optional[int]
    name: Optional[str]
    genres: Optional[str]
    spotify_id: Optional[str]
    spotify_popularity: Optional[int]
    image: Optional[str]
