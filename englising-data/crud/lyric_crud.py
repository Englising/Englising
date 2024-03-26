from dto.lyric_dto import LyricDto
from model.lyric import Lyric
from model.track import Track
from model.album import Album
from model.artist_track import ArtistTrack
from model.artist import Artist
from model.artist_album import ArtistAlbum


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


def get_lyrics_by_track_id(track_id: int, session):
    return session.query(Lyric).filter(Lyric.track_id == track_id).all()


def get_lyric_dtos_by_track_id(track_id: int, session):
    lyrics = session.query(Lyric).filter(Lyric.track_id == track_id).all()
    return [LyricDto(
        lyric_id=lyric.lyric_id,
        track_id=lyric.track_id,
        start_time=float(lyric.start_time),
        end_time=float(lyric.end_time),
        en_text=lyric.en_text,
        kr_text=lyric.kr_text
    ) for lyric in lyrics]