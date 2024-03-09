from _pydecimal import Decimal
from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel


class LyricDto(BaseModel):
    lyric_id: Optional[int] = None
    start_time: Optional[Decimal] = None
    end_time: Optional[Decimal] = None
    en_text: Optional[str] = None
    kr_text: Optional[datetime] = None