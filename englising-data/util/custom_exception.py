

class AlbumException(Exception):
    def __init__(self, message="An error occurred with an album operation"):
        super().__init__(message)


class ArtistException(Exception):
    def __init__(self, message="An error occurred with an artist operation"):
        super().__init__(message)


class TrackException(Exception):
    def __init__(self, message="An error occurred with a track operation"):
        super().__init__(message)


class LyricException(Exception):
    def __init__(self, message="An error occurred with a lyric operation"):
        super().__init__(message)
