from model.lyric import Lyric


def create_lyric(lyric: Lyric, session):
    session.add(lyric)
    session.flush()
    session.refresh(lyric)
    return lyric


def get_lyrics_without_kr(session):
    result = session.query(Lyric).filter(Lyric.kr_text == None).all()
    return result


def update_lyric_kr_text(lyric: Lyric, session):
    session.query(Lyric).filter(Lyric.lyric_id == lyric.lyric_id).update({'kr_text': lyric.kr_text})
    session.flush()

