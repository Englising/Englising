from dto.lyric_dto import LyricDto
from model.lyric import Lyric


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
