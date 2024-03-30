from operator import or_
from typing import List

from database.mysql_manager import Session
from dto.track_dto import YoutubeQueryDto
from model.artist import Artist
from model.lyric import Lyric
from model.track import Track


def create_track(track: Track, session):
    session.add(track)
    session.flush()
    session.refresh(track)
    return track


def update_track_is_english(track_id: int, is_english: bool, session):
    session.query(Track).filter(Track.track_id == track_id).update({"is_english": is_english})
    session.flush()


def get_track_by_spotify_id(spotify_id: str, session):
    result = session.query(Track).filter(Track.spotify_id == spotify_id).one_or_none()
    return result


def get_track_by_track_id(track_id: int, session):
    result = session.query(Track).filter(Track.track_id == track_id).one_or_none()
    return result


def get_tracks_without_lyrics_and_not_only_english(session: Session) -> List[Track]:
    return (session.query(Track)
            .outerjoin(Lyric, Track.track_id == Lyric.track_id)
            .filter(Lyric.track_id.is_(None))
            .filter(or_(Track.is_english.is_(None), Track.is_english.is_(True)))
            .limit(300)
            .all())