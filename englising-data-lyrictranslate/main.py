import threading

from core_service.lyric_translate_service import LyricTranslateWorker


def main():
    lyric_translate_worker = LyricTranslateWorker()
    lyric_translate_thread = threading.Thread(target=lyric_translate_worker.start)
    lyric_translate_thread.start()
    lyric_translate_thread.join()


if __name__ == "__main__":
    main()
