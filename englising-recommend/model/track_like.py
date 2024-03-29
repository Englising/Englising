from sqlalchemy import Column, ForeignKey, Integer, String, Float, Boolean, TIMESTAMP, DECIMAL, Text, Date, Table
from sqlalchemy.sql import func
from model.base import Base

class TrackLike(Base):
    __tablename__ = 'track_like'
    track_like_id = Column(Integer, primary_key=True)
    track_id = Column(Integer, ForeignKey('track.track_id'), nullable=False)
    user_id = Column(Integer, ForeignKey('user.user_id'), nullable=False)
    is_liked = Column(Boolean, nullable=False)
    created_at = Column(TIMESTAMP, nullable=False, default=func.now())
    updated_at = Column(TIMESTAMP, onupdate=func.now())

    def print(self):
        print(f"Track Like ID: {self.track_like_id}")
        print(f"Track ID: {self.track_id}")
        print(f"User ID: {self.user_id}")
        print(f"Is Like: {self.is_liked}")
        print(f"Created At: {self.created_at}")