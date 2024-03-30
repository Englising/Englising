import threading

from core_service.lyric_gather_service import LyricWorker


def main():
    lyric_worker = LyricWorker()
    lyric_thread = threading.Thread(target=lyric_worker.start)
    lyric_thread.start()
    lyric_thread.join()


if __name__ == "__main__":
    main()
