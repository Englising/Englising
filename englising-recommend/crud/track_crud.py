from typing import List

from database.mysql_manager import Session
from model import Track


def get_all_tracks(session: Session) -> List[Track]:
    return session.query(Track) \
        .join(Track.track_words) \
        .filter(Track.youtube_id != None) \
        .filter(Track.genre != None) \
        .filter(Track.lyric_status == 'DONE') \
        .distinct().all()

