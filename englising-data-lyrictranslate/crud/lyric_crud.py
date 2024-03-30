from typing import List

from model.lyric import Lyric


def create_lyric(lyric: Lyric, session):
    session.add(lyric)
    session.flush()
    session.refresh(lyric)
    return lyric


def get_track_ids_without_kr_lyric(session):
    return session.query(Lyric.track_id).filter(Lyric.kr_text == None).distinct(Lyric.track_id).limit(100)


def get_lyrics_by_track_id(session, track_id: int) -> List[Lyric]:
    result = session.query(Lyric).filter(Lyric.track_id == track_id).all()
    for lyric in result:
        print(lyric.en_text)
    return result


def get_lyrics_without_kr(session):
    result = session.query(Lyric.track_id).filter(Lyric.kr_text == None).distinct().limit(100)
    return result


def update_lyric_kr_text(lyric: Lyric, session):
    session.query(Lyric).filter(Lyric.lyric_id == lyric.lyric_id).update({'kr_text': lyric.kr_text})
    session.flush()

