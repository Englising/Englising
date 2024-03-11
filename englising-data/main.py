import time

import redis
import threading

from core_service.album_gather_service import AlbumWorker
from core_service.artist_gather_service import ArtistWorker
from core_service.track_gather_service import TrackWorker
from core_service.lyric_gather_service import LyricWorker
from core_service.save_service import SaveWorker

from util.worklist import WorkList
from log.log_info import LogList, LogKind
from log.englising_logger import log


def main():
    starting_year = 2023
    redis_connection = redis.Redis(host='localhost', port=6379, db=0)
    redis_connection.delete(WorkList.ALBUM.name)
    redis_connection.delete(WorkList.ARTIST.name)
    redis_connection.delete(WorkList.TRACK.name)
    redis_connection.delete(WorkList.LYRICS.name)
    redis_connection.delete(WorkList.SAVE.name)

    redis_connection.rpush(WorkList.ALBUM.name, starting_year)

    album_worker = AlbumWorker(redis_host='localhost', redis_port=6379, redis_db=0, queue_name=WorkList.ALBUM.name)
    artist_worker = ArtistWorker()
    track_worker = TrackWorker()
    lyric_worker = LyricWorker()
    save_worker = SaveWorker()

    album_thread = threading.Thread(target=album_worker.start)
    artist_thread = threading.Thread(target=artist_worker.start)
    track_thread = threading.Thread(target=track_worker.start)
    lyric_thread = threading.Thread(target=lyric_worker.start)
    save_thread = threading.Thread(target=save_worker.start)

    album_thread.start()
    artist_thread.start()
    track_thread.start()
    lyric_thread.start()
    save_thread.start()

    album_thread.join()
    artist_thread.join()
    track_thread.join()
    lyric_thread.join()
    save_thread.join()

if __name__ == "__main__":
    main()