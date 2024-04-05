import json
import logging
import math
import urllib.error
import urllib.parse
import urllib.request
from _pydecimal import Decimal
from typing import List

from dotenv import load_dotenv
import os

from dto.lyric_dto import LyricDto
from log.log_info import LogList, LogKind
from log.englising_logger import log
from util.custom_exception import LyricException

from dto.track_dto import MusixMatchDto

load_dotenv()

MUSIX_TOKEN = os.getenv('MUSIX_TOKEN')
MUSIX_URL = os.getenv('MUSIX_URL')

headers = {
    "authority": os.getenv("MUSIX_AUTHORITY"),
    "cookie": os.getenv("MUSIX_COOKIE")
}


def find_lyrics(musixmatch_dto:MusixMatchDto) -> List[LyricDto]:
    duration = musixmatch_dto.track_duration / 1000 if musixmatch_dto.track_duration else 0
    params = {
        "q_album": musixmatch_dto.album,
        "q_artist": musixmatch_dto.artist,
        "q_artists": musixmatch_dto.artist,
        "q_track": musixmatch_dto.track_name,
        "track_spotify_id": musixmatch_dto.track_spotify_id,
        "q_duration": str(duration),
        "f_subtitle_length": str(math.floor(duration)) if duration > 0 else "",
        "usertoken": MUSIX_TOKEN,
    }
    log(LogList.MUSIX.name, LogKind.INFO, f"Finding lyrics for {musixmatch_dto.track_name}")
    req = urllib.request.Request(MUSIX_URL + urllib.parse.urlencode(params, quote_via=urllib.parse.quote),
                                 headers=headers)
    try:
        response = urllib.request.urlopen(req).read()
    except (urllib.error.HTTPError, urllib.error.URLError, ConnectionResetError) as e:
        log(LogList.MUSIX.name, LogKind.ERROR, e)
        return

    r = json.loads(response.decode())
    if r['message']['header']['status_code'] != 200 and r['message']['header'].get('hint') == 'renew':
        log(LogList.MUSIX.name, LogKind.ERROR, "Invalid token")
        return
    body = r["message"]["body"]["macro_calls"]

    if body["matcher.track.get"]["message"]["header"]["status_code"] != 200:
        if body["matcher.track.get"]["message"]["header"]["status_code"] == 404:
            log(LogList.MUSIX.name, LogKind.INFO, "Song not found.")
            raise LyricException("DROP", musixmatch_dto.track_spotify_id)
        elif body["matcher.track.get"]["message"]["header"]["status_code"] == 401:
            log(LogList.MUSIX.name, LogKind.WARNING, "Timed out. Change the token or wait a few minutes before trying again.")
            raise LyricException("TIMEOUT")
        else:
            log(LogList.MUSIX.name, LogKind.ERROR, f'Requested error: {body["matcher.track.get"]["message"]["header"]}')
            raise LyricException("DROP", musixmatch_dto.track_spotify_id)
    if isinstance(body["track.lyrics.get"]["message"].get("body"), dict):
        if body["track.lyrics.get"]["message"]["body"]["lyrics"]["restricted"]:
            log(LogList.MUSIX.name, LogKind.INFO, "Restricted lyrics.")
            raise LyricException("DROP", musixmatch_dto.track_spotify_id)
    if body["matcher.track.get"]["message"]["body"]["track"]["has_richsync"] == 0:
        log(LogList.MUSIX.name, LogKind.INFO, "No richsync found.")
        raise LyricException("DROP", musixmatch_dto.track_spotify_id)

    return musixmatch_reqult_to_lyric(body['track.subtitles.get']['message']['body']['subtitle_list'][0])


def musixmatch_reqult_to_lyric(body) -> List[LyricDto]:
    lyrics = []
    subtitles = json.loads(body['subtitle']['subtitle_body'])
    for i, subtitle in enumerate(subtitles):
        text = subtitle['text']
        start_time = Decimal(str(subtitle['time']['total']))
        if i < len(subtitles) - 1:
            end_time = Decimal(str(subtitles[i + 1]['time']['total']))
        else:
            end_time = start_time
        if(text != ""):
            lyrics.append(LyricDto(
                start_time = start_time,
                end_time = end_time,
                en_text = text
            ))
    return lyrics
