from googletrans import Translator

from log.englising_logger import log
from log.log_info import LogList, LogKind

translator = Translator()


def get_translation(text):
    log(LogList.GOOGLE.name, LogKind.INFO, f"Getting Translation for {text}")
    try:
        return translator.translate(text, src="en", dest="ko").text
    except Exception as e:
        log(LogList.GOOGLE.name, LogKind.ERROR, f"Failed Getting Translation for {text}: {str(e)}")


def detect_lyric_language(text) -> bool:
    log(LogList.GOOGLE.name, LogKind.INFO, f"Getting detection for {text}")
    try:
        detection = translator.detect(text)
        if detection is None:
            return False
        if detection.lang == "en":
            return True
        else:
            return False
    except Exception as e:
        log(LogList.GOOGLE.name, LogKind.ERROR, f"Failed detection for {text}: {str(e)}")
        return False

