from typing import List
import re

from dotenv import load_dotenv
import os
from googleapiclient.discovery import build
from dto.track_dto import YoutubeDto

from log.log_info import LogList, LogKind
from log.englising_logger import log
from util.custom_exception import TrackException, YoutubeException

load_dotenv()

YOUTUBE_API_KEY = os.getenv("YOUTUBE_API_KEY")
YOUTUBE_API_SERVICE_NAME="youtube"
YOUTUBE_API_VERSION="v3"

youtube = build(YOUTUBE_API_SERVICE_NAME,YOUTUBE_API_VERSION, developerKey=YOUTUBE_API_KEY)


def search_youtube(track_title, artist_name, duration_ms) -> YoutubeDto:
    log(LogList.YOUTUBE.name, LogKind.INFO, f"Searching {track_title} {artist_name}")
    try:
        youtube_ids = []
        search_response = youtube.search().list(
            q=f"{track_title} {artist_name} official audio",
            order='relevance',
            part='snippet',
            maxResults=10,
            ).execute()
        for item in search_response.get("items", []):
            youtube_ids.append(item["id"]["videoId"])
        youtube_list = get_video_from_youtube_ids(youtube_ids)
        return figure_closest_time(youtube_list, duration_ms)
    except Exception as e:
        log(LogList.YOUTUBE.name, LogKind.ERROR, f"Failed Searching {track_title} {artist_name} {e}")
        raise YoutubeException()


def get_video_from_youtube_ids(youtube_ids: List[str]):
    youtube_list = []
    for id in youtube_ids:
        youtube_list.append(get_video_from_youtube_id(id))
    return youtube_list


def get_video_from_youtube_id(youtube_id: str):
    log(LogList.YOUTUBE.name, LogKind.INFO, f"Getting {youtube_id}")
    try:
        request = youtube.videos().list(
            part="id,snippet,contentDetails,status",
            id=youtube_id
        ).execute()["items"][0]
        status = request["status"]
        if not status["embeddable"] or status["privacyStatus"] != "public":
            log(LogList.YOUTUBE.name, LogKind.WARNING, f"{youtube_id} is not embeddable or not public")
            raise YoutubeException("Video is not embeddable or not public")

        duration_ms = convert_duration_to_ms(request["contentDetails"]["duration"])
        return YoutubeDto(youtube_id=request["id"], duration_ms=duration_ms)
    except Exception as e:
        log(LogList.YOUTUBE, LogKind.ERROR, f"Failed Getting {youtube_id} {str(e)}")
        raise YoutubeException()


def convert_duration_to_ms(duration: str) -> int:
    pattern = re.compile(r'PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?')
    match = pattern.match(duration)
    hours, minutes, seconds = 0, 0, 0
    if match:
        hours = int(match.group(1)) if match.group(1) else 0
        minutes = int(match.group(2)) if match.group(2) else 0
        seconds = int(match.group(3)) if match.group(3) else 0
    ms = (hours * 3600 + minutes * 60 + seconds) * 1000
    return ms


def figure_closest_time(youtube_list, duration_ms: int):
    closest_youtube = None
    closest_time = None
    for youtube in youtube_list:
        diff = abs(youtube.duration_ms - duration_ms)
        if closest_time is None or diff < closest_time:
            closest_youtube = youtube
            closest_time = diff
    if closest_time > 1000:
        log(LogList.YOUTUBE.name, LogKind.WARNING, f"Can't figure out Close video closest={closest_time}, duration={duration_ms}")
        return None
    return closest_youtube

