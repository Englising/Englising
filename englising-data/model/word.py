from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from model.base import Base


class Word(Base):
    __tablename__ = 'word'
    word_id = Column(Integer, primary_key=True)
    en_text = Column(String(255), nullable=False)
    ko_text = Column(String(255), nullable=True)
    example = Column(String(512), nullable=False)
    created_at = Column(TIMESTAMP, default=func.now(), nullable=False)
    updated_at = Column(TIMESTAMP, onupdate=func.now())
    ko_text_1 = Column(String(255), nullable=True)
    ko_text_2 = Column(String(255), nullable=True)
    ko_text_3 = Column(String(255), nullable=True)

    track_words = relationship("TrackWord", back_populates="word")
