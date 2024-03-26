import time

import redis
import threading

from core_service.album_gather_service import AlbumWorker
from core_service.artist_gather_service import ArtistWorker
from core_service.lyric_translate_service import LyricTranslateWorker
from core_service.track_gather_service import TrackWorker
from core_service.lyric_gather_service import LyricWorker
from core_service.word_split_service import LyricWordWorker
from core_service.youtube_gather_service import YoutubeWorker
from crud.track_crud import get_tracks_without_words
from database.mysql_manager import Session


def main():
    print("build complete")
    album_worker = AlbumWorker()
    artist_worker = ArtistWorker()
    track_worker = TrackWorker()
    youtube_worker = YoutubeWorker()
    lyric_worker = LyricWorker()
    lyric_translate_worker = LyricTranslateWorker()
    lyric_word_worker = LyricWordWorker()

    album_thread = threading.Thread(target=album_worker.start)
    artist_thread = threading.Thread(target=artist_worker.start)
    track_thread = threading.Thread(target=track_worker.start)
    youtube_thread = threading.Thread(target=youtube_worker.start)
    lyric_thread = threading.Thread(target=lyric_worker.start)
    lyric_translate_thread = threading.Thread(target=lyric_translate_worker.start)
    lyric_word_thread = threading.Thread(target=lyric_word_worker.start)

    # album_thread.start()
    artist_thread.start()
    track_thread.start()
    youtube_thread.start()
    lyric_thread.start()
    lyric_translate_thread.start()
    lyric_word_thread.start()

    # album_thread.join()
    artist_thread.join()
    track_thread.join()
    youtube_thread.join()
    lyric_thread.join()
    lyric_translate_thread.join()


if __name__ == "__main__":
    main()
