from enum import Enum


class LogList(Enum):
    SPOTIFY = "SPOTIFY"
    ALBUM = "ALBUM"
    ARTIST = "ARTIST"
    TRACK = "TRACK"
    LYRICS = "LYRICS"
    YOUTUBE = "YOUTUBE"


class LogKind:
    INFO = "INFO"
    WARNING = "WARNING"
    ERROR = "ERROR"
    CRITICAL = "CRITICAL"
    DEBUG = "DEBUG"
    TRACE = "TRACE"
