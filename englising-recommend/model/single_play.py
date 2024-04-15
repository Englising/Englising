from sqlalchemy import Column, Integer, ForeignKey, String, TIMESTAMP, Boolean, SmallInteger
from sqlalchemy.sql import func
from model.base import Base

class Singleplay(Base):
    __tablename__ = 'singleplay'

    singleplay_id = Column(Integer, primary_key=True, autoincrement=True)
    singleplay_level_id = Column(SmallInteger, ForeignKey('singleplay_hint.singleplay_level_id'), nullable=False)
    user_id = Column(Integer, ForeignKey('user.user_id'), nullable=False)
    track_id = Column(Integer, ForeignKey('track.track_id'), nullable=False)
    score = Column(SmallInteger, nullable=True)
    correct_rate = Column(SmallInteger, nullable=True)
    created_at = Column(TIMESTAMP, default=func.now(), nullable=False)
    updated_at = Column(TIMESTAMP, onupdate=func.now(), nullable=True)
