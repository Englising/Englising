from fastapi import APIRouter

single_play_router = APIRouter(
    prefix="/singleplay",
)


@single_play_router.get("/game/{trackId}")
async def getSinglePlayGame(trackId: str):
    return {"message": "Hello World"}

