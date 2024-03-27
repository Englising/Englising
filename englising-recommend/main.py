from fastapi import FastAPI
import uvicorn
from controller.single_play_router import single_play_router

app = FastAPI()
app.include_router(single_play_router)
