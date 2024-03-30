from typing import Optional
from pydantic import BaseModel


class WordDto(BaseModel):
    word_id: Optional[int] = None
    en_text: Optional[str] = None
    ko_text_1: Optional[str] = None
    ko_text_2: Optional[str] = None
    ko_text_3: Optional[str] = None
    example: Optional[str] = None
