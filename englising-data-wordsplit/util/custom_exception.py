

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
    def __init__(self, message="An error occurred with a lyric operation", track_spotify_id=0):
        super().__init__(message)
        self.track_spotify_id = track_spotify_id


class YoutubeException(Exception):
    def __init__(self, message="An error occurred with a youtube operation"):
        super().__init__(message)


class GoogleException(Exception):
    def __init__(self, message="An error occurred with a google operation"):
        super().__init__(message)
