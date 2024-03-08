from sqlalchemy import Column, ForeignKey, Integer, TIMESTAMP
from sqlalchemy.sql import func
from sqlalchemy.schema import UniqueConstraint
from model.base import Base


class ArtistAlbum(Base):
    __tablename__ = 'artist_album'
    artist_album_id = Column(Integer, primary_key=True, autoincrement=True)
    album_id = Column(Integer, ForeignKey('album.album_id'), nullable=False)
    artist_id = Column(Integer, ForeignKey('artist.artist_id'), nullable=False)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, nullable=True, onupdate=func.now())

    __table_args__ = (UniqueConstraint('album_id', 'artist_id', name='_album_artist_uc'),)
