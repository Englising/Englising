from model.lyric import Lyric
from model.track import Track
from model.word import Word
from model.track_word import TrackWord


def create_word(word: Word, session):
    session.add(word)
    session.flush()
    session.refresh(word)
    return word


def find_word_by_en_text(en_text:str, session):
    return session.query(Word).filter(Word.en_text == en_text).first()
