from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from model.base import Base


class TrackWord(Base):
    __tablename__ = 'track_word'
    track_word_id = Column(Integer, primary_key=True)
    track_id = Column(Integer, ForeignKey('track.track_id'), nullable=False)
    lyric_id = Column(Integer, ForeignKey('lyric.lyric_id'), nullable=False)
    word_id = Column(Integer, ForeignKey('word.word_id'), nullable=False)
    word_index = Column(Integer, nullable=False)
    origin_word = Column(String, nullable=False)
    importance = Column(Float, nullable=True)
    created_at = Column(TIMESTAMP, default=func.now())
    updated_at = Column(TIMESTAMP, onupdate=func.now())

    track = relationship("Track", back_populates="track_words")
    lyric = relationship("Lyric", back_populates="track_words")
    word = relationship("Word", back_populates="track_words")
