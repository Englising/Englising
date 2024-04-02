from enum import Enum


class LogList(Enum):
    SPOTIFY = "SPOTIFY"
    ALBUM = "ALBUM"
    ARTIST = "ARTIST"
    TRACK = "TRACK"
    LYRICS = "LYRICS"
    YOUTUBE = "YOUTUBE"
    MUSIX = "MUSIX"
    PAPAGO = "PAPAGO"
    SAVE = "SAVE"
    GOOGLE = "GOOGLE"
    TRANSLATE = "TRANSLATE"
    LYRIC_WORD = "LYRIC_WORD"
    SEARCH_SERVICE = "SEARCH_SERVICE"


class LogKind:
    INFO = "INFO"
    WARNING = "WARNING"
    ERROR = "ERROR"
    CRITICAL = "CRITICAL"
    DEBUG = "DEBUG"
    TRACE = "TRACE"
