from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from model.base import Base

class Lyric(Base):
    __tablename__ = 'lyric'
    lyric_id = Column(Integer, primary_key=True)
    track_id = Column(Integer, ForeignKey('track.track_id'), nullable=False)
    start_time = Column(DECIMAL(10, 3), nullable=False)
    end_time = Column(DECIMAL(10, 3), nullable=False)
    en_text = Column(Text, nullable=False)
    kr_text = Column(Text, nullable=False)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, onupdate=func.now())

    track = relationship("Track", back_populates="lyrics")

