from typing import Optional, List
from pydantic import BaseModel


class LyricDto(BaseModel):
    lyricId: Optional[int]
    isBlank: Optional[bool] = False
    startTime: Optional[float] = None
    endTime: Optional[float] = None
    lyric: Optional[List[str]] = None