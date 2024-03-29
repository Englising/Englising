from fastapi import FastAPI
from controller.single_play_router import single_play_router
from config.fast_text_config import fast_text_model

app = FastAPI()

app.include_router(single_play_router)