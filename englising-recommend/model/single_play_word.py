from sqlalchemy import Column, Integer, ForeignKey, String, TIMESTAMP, Boolean
from sqlalchemy.sql import func
from model.base import Base


class SingleplayWord(Base):
    __tablename__ = 'singleplay_word'

    singleplay_word_id = Column(Integer, primary_key=True, autoincrement=True)
    singleplay_id = Column(Integer, ForeignKey('singleplay.singleplay_id'), nullable=False)
    word_id = Column(Integer, ForeignKey('word.word_id'), nullable=False)
    lyric_id = Column(Integer, ForeignKey('lyric.lyric_id'), nullable=True)
    sentence_index = Column(Integer, nullable=True)
    word_index = Column(Integer, nullable=True)
    origin_word = Column(String(255), nullable=True)
    is_right = Column(Boolean, nullable=True)
    created_at = Column(TIMESTAMP, default=func.now(), nullable=False)
    updated_at = Column(TIMESTAMP, onupdate=func.now(), nullable=True)

