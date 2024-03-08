from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from models.base import Base


class ArtistTrack(Base):
    __tablename__ = 'artist_track'
    artist_id = Column(Integer, ForeignKey('artist.artist_id'), primary_key=True)
    track_id = Column(Integer, ForeignKey('track.track_id'), primary_key=True)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, nullable=True, onupdate=func.now())
    artist = relationship("Artist", back_populates="tracks")
    track = relationship("Track", back_populates="artists")