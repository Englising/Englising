import json
import logging
import math
import urllib.error
import urllib.parse
import urllib.request

from dotenv import load_dotenv
import os

from dto.track_dto import MusixMatchDto

load_dotenv()

MUSIX_TOKEN = os.getenv('MUSIX_TOKEN')
MUSIX_URL = os.getenv('MUSIX_URL')

headers = {
    "authority": os.getenv("AUTHORITY"),
    "cookie": os.getenv("COOKIE")
}


def find_lyrics(musixmatch_dto:MusixMatchDto):
    duration = musixmatch_dto.track_duration / 1000 if musixmatch_dto.track_duration else ""
    params = {
        "q_album": musixmatch_dto.album,
        "q_artist": musixmatch_dto.artist,
        "q_artists": musixmatch_dto.artist,
        "q_track": musixmatch_dto.track_name,
        "track_spotify_id": musixmatch_dto.track_spotify_id,
        "q_duration": duration,
        "f_subtitle_length": math.floor(duration) if duration else "",
        "usertoken": MUSIX_TOKEN,
    }

    req = urllib.request.Request(MUSIX_URL + urllib.parse.urlencode(params, quote_via=urllib.parse.quote),
                                 headers=headers)
    try:
        response = urllib.request.urlopen(req).read()
    except (urllib.error.HTTPError, urllib.error.URLError, ConnectionResetError) as e:
        logging.error("[MUSIXMATCH LOG] : "+repr(e))
        return

    r = json.loads(response.decode())
    if r['message']['header']['status_code'] != 200 and r['message']['header'].get('hint') == 'renew':
        logging.error("[MUSIXMATCH LOG] : Invalid token")
        return
    body = r["message"]["body"]["macro_calls"]

    if body["matcher.track.get"]["message"]["header"]["status_code"] != 200:
        if body["matcher.track.get"]["message"]["header"]["status_code"] == 404:
            logging.info('Song not found.')
        elif body["matcher.track.get"]["message"]["header"]["status_code"] == 401:
            logging.warning('[MUSIXMATCH LOG] : Timed out. Change the token or wait a few minutes before trying again.')
        else:
            logging.error(f'Requested error: {body["matcher.track.get"]["message"]["header"]}')
        return
    elif isinstance(body["track.lyrics.get"]["message"].get("body"), dict):
        if body["track.lyrics.get"]["message"]["body"]["lyrics"]["restricted"]:
            logging.info("[MUSIXMATCH LOG] : Restricted lyrics.")
            return
    elif body["matcher.track.get"]["message"]["body"]["track"]["has_richsync"] == 0:
        logging.info("[MUSIXMATCH LOG] : No richsync found.")
        return
    print("로그"+str(body['track.subtitles.get']['message']['body']['subtitle_list'][0]))
    return body['track.subtitles.get']['message']['body']['subtitle_list'][0]