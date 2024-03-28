from fastapi import APIRouter

import service.single_play_service as single_play_service

single_play_router = APIRouter(
    prefix="/singleplay",
)


@single_play_router.get("/word/{track_id}")
async def get_single_play_words(track_id: int):
    return single_play_service.get_lyrics_from_track_id(track_id)


# track_id의 단어 모두 가져오기
# 