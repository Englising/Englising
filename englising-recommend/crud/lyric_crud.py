from typing import List

from model import Lyric


def get_lyrics_by_track_id(session, track_id: int) -> List[Lyric]:
    return session.query(Lyric).filter(Lyric.track_id == track_id).all()
