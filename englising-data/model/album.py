from sqlalchemy import Column, Integer, String, TIMESTAMP, Text, Date
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from model.base import Base

class Album(Base):
    __tablename__ = 'album'
    album_id = Column(Integer, primary_key=True)
    title = Column(String(255), nullable=False)
    type = Column(String(255))
    total_tracks = Column(Integer, nullable=False)
    spotify_id = Column(String(255), nullable=False)
    cover_image = Column(String(512), nullable=False)
    release_date = Column(Date)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, onupdate=func.now())
    tracks = relationship("Track", back_populates="album")