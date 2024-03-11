import json
import urllib.request
from dotenv import load_dotenv
import os

from log.log_info import LogList, LogKind
from log.englising_logger import log
from util.custom_exception import LyricException

load_dotenv()
PAPAGO_TRANSLATION_URL = os.getenv("PAPAGO_TRANSLATION_URL")
PAPAGO_LANGUAGE_DETECTION_URL = os.getenv("PAPAGO_LANGUAGE_DETECTION_URL")
PAPAGO_CLIENT_ID = os.getenv("PAPAGO_CLIENT_ID")
PAPAGO_CLIENT_SECRET = os.getenv("PAPAGO_CLIENT_SECRET")

headers = {
    "X-NCP-APIGW-API-KEY-ID": PAPAGO_CLIENT_ID,
    "X-NCP-APIGW-API-KEY": PAPAGO_CLIENT_SECRET,
    "Content-Type": "application/x-www-form-urlencoded"
}


def get_lyric_translation(text, spotify_id) -> str:
    log(LogList.PAPAGO.name, LogKind.INFO, f"Getting Translation for {text}")
    encText = urllib.parse.quote(text)
    data = "source=en&target=ko&text=" + encText
    request = urllib.request.Request(PAPAGO_TRANSLATION_URL, data=data.encode("utf-8"), headers=headers)
    try:
        response = urllib.request.urlopen(request)
        rescode = response.getcode()
        if rescode == 200:
            response_body = json.loads(response.read().decode("utf-8"))
            return response_body["message"]["result"]["translatedText"]
        else:
            log(LogList.PAPAGO.name, LogKind.ERROR, "Error Code:" + rescode)
            raise LyricException("TIMEOUT")
    except Exception as e:
        log(LogList.PAPAGO.name, LogKind.ERROR, e.reason)
        raise LyricException("TIMEOUT")


def detect_lyric_language(text) -> bool:
    encQuery = urllib.parse.quote(text)
    data = "query=" + encQuery
    request = urllib.request.Request(PAPAGO_LANGUAGE_DETECTION_URL, headers=headers)
    try:
        response = urllib.request.urlopen(request, data=data.encode("utf-8"))
        rescode = response.getcode()
        if (rescode == 200):
            response_body = json.loads(response.read().decode("utf-8"))
            lang = response_body["langCode"]
            if lang == "en":
                return True
            else:
                return False
        else:
            log(LogList.PAPAGO.name, LogKind.ERROR, "Error Code:" + rescode)
            raise LyricException("TIMEOUT")
    except Exception as e:
        log(LogList.PAPAGO.name, LogKind.ERROR, e.reason)
        raise LyricException("TIMEOUT")
