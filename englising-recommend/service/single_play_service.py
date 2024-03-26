from typing import List

from nltk.tokenize import word_tokenize

import crud.lyric_crud as lyric_crud
from database.mysql_manager import Session
from dto.lyric_dto import LyricDto
from model import Lyric

session = Session()


def get_lyrics_from_track_id(track_id: int):
    lyrics = lyric_crud.get_lyrics_by_track_id(session, track_id)
    return list(map(get_lyric_into_words, lyrics))


def get_lyric_into_words(lyric: Lyric) -> List[LyricDto]:
    splitted_words = lyric.en_text.split(" ")
    finished_words = []
    for index, word in enumerate(splitted_words):
        if index != 0:
            finished_words.append(" ")
        tokenized_words = word_tokenize(word)
        finished_words.extend(tokenized_words)
    return LyricDto(
        isBlank = False,
        startTime = lyric.start_time,
        endTime = lyric.end_time,
        lyric = finished_words
    )
