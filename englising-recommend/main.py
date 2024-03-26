from fastapi import FastAPI

from controller.single_play_router import single_play_router

app = FastAPI()
app.include_router(single_play_router)