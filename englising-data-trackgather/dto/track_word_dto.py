from typing import Optional
from pydantic import BaseModel


class TrackWordDto(BaseModel):
    track_word_id: Optional[int] = None
    track_id: Optional[int] = None
    lyric_id: Optional[int] = None
    word_id: Optional[int] = None
    word_index: Optional[int] = None
    importance: Optional[float] = None