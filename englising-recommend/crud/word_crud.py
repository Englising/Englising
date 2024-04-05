from typing import List

from database.mysql_manager import Session
from model import Track, TrackWord, Word, WordLike
from model.single_play import Singleplay
from model.single_play_word import SingleplayWord
from typing import List


def get_all_track_words_by_track_id(session:Session, track_id: int) -> List[TrackWord]:
    return session.query(TrackWord).filter(TrackWord.track_id == track_id).all()


def get_liked_words_by_user_id(session: Session, user_id: int) -> List[Word]:
    liked_words_query = session.query(Word).\
        join(WordLike, WordLike.word_id == Word.word_id).\
        filter(WordLike.user_id == user_id).\
        filter(WordLike.is_liked == True)
    liked_words = liked_words_query.all()
    return liked_words


def get_recently_played_words_by_user_id(session: Session, user_id: int) -> List[Word]:
    recent_singleplay = session.query(Singleplay). \
        filter(Singleplay.user_id == user_id). \
        order_by(Singleplay.created_at.desc()). \
        first()

    if recent_singleplay is None:
        return []

    word_ids = session.query(SingleplayWord.word_id). \
        filter(SingleplayWord.singleplay_id == recent_singleplay.singleplay_id). \
        all()

    word_ids = [word_id[0] for word_id in word_ids]

    recently_played_words = session.query(Word). \
        filter(Word.word_id.in_(word_ids)). \
        all()
    return recently_played_words
