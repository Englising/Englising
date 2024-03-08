from _pydecimal import Decimal
from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel


class LyricDto(BaseModel):
    lyric_id: Optional[int]
    start_time: Optional[Decimal]
    end_time: Optional[Decimal]
    en_text: Optional[str]
    kr_text: Optional[datetime]