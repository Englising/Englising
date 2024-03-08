from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from model.base import Base


class Track(Base):
    __tablename__ = 'track'
    track_id = Column(Integer, primary_key=True)
    album_id = Column(Integer, ForeignKey('album.album_id'), nullable=False)
    track_index = Column(Integer, nullable=False)
    title = Column(String(255), nullable=False)
    spotify_id = Column(String(255), nullable=False)
    youtube_id = Column(String(255), nullable=False)
    isrc = Column(String(255), nullable=False)
    spotify_popularity = Column(Integer, nullable=False)
    duration_ms = Column(Integer, nullable=False)
    is_lyrics = Column(Boolean, nullable=False, default=False)
    feature_acousticness = Column(Float, nullable=True)
    feature_danceability = Column(Float, nullable=True)
    feature_energy = Column(Float, nullable=True)
    feature_tempo = Column(Float, nullable=True)
    feature_positiveness = Column(Float, nullable=True)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, onupdate=func.now())
    album = relationship("Album", back_populates="tracks")
    artists = relationship("ArtistTrack", back_populates="track")
