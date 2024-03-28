from sqlalchemy import Column, Integer, ForeignKey, TIMESTAMP, Boolean
from sqlalchemy.sql import func
from model.base import Base


class WordLike(Base):
    __tablename__ = 'word_like'

    word_like_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey('user.user_id'), nullable=False)
    word_id = Column(Integer, ForeignKey('word.word_id'), nullable=False)
    is_liked = Column(Boolean, default=False, nullable=False)
    created_at = Column(TIMESTAMP, default=func.now(), nullable=False)
    updated_at = Column(TIMESTAMP, onupdate=func.now(), nullable=True)