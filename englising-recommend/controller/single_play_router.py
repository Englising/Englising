from typing import List
from fastapi import APIRouter
from fastapi.params import Query

import service.single_play_service as single_play_service
import service.single_play_playlist_recommend_service as single_play_recommend_service
import service.single_play_word_service as single_play_word_service
from dto.lyric_dto import LyricDto
from model import TrackWord

single_play_router = APIRouter(
    prefix="/singleplay",
)


@single_play_router.get("/game/lyrics/{track_id}")
async def get_single_play_game(track_id: int)-> list[LyricDto]:
    return single_play_service.get_lyrics_from_track_id(track_id)


@single_play_router.get("/playlist/recommend/{user_id}")
async def get_single_play_recommend(user_id: int) -> List[int]:
    return single_play_recommend_service.get_recommend_for_user_id(user_id)


@single_play_router.get("/game/words", response_model=None)
async def get_game_words(track_id: int = Query(..., alias="track-id"), user_id: int = Query(..., alias="user-id"), level: int = Query(..., alias="level")) -> List[TrackWord]:
    return single_play_word_service.get_recommended_track_words(user_id, track_id, level)
