from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from model.base import Base


class ArtistTrack(Base):
    __tablename__ = 'artist_track'
    artist_track_id = Column(Integer, primary_key=True, autoincrement=True)
    artist_id = Column(Integer, ForeignKey('artist.artist_id'), nullable=False)
    track_id = Column(Integer, ForeignKey('track.track_id'), nullable=False)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, nullable=True, onupdate=func.now())
