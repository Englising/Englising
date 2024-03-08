from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from models.base import Base


class ArtistAlbum(Base):
    __tablename__ = 'artist_album'
    album_id = Column(Integer, ForeignKey('album.album_id'), primary_key=True)
    artist_id = Column(Integer, ForeignKey('artist.artist_id'), primary_key=True)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, onupdate=func.now())