from _pydecimal import Decimal
from datetime import datetime
from typing import Optional
from pydantic import BaseModel


class LyricDto(BaseModel):
    lyric_id: Optional[int] = None
    start_time: Optional[float] = None
    end_time: Optional[float] = None
    en_text: Optional[str] = None
    kr_text: Optional[str] = None
