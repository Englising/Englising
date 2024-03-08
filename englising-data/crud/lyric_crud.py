from model.lyric import Lyric


def create_lyric(lyric: Lyric, session):
    session.add(lyric)
    session.flush()
    session.refresh(lyric)
    return lyric

