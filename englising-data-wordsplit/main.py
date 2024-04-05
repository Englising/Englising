
import threading

from core_service.word_split_service import LyricWordWorker


def main():
    lyric_word_worker = LyricWordWorker()
    lyric_word_thread = threading.Thread(target=lyric_word_worker.start)
    lyric_word_thread.start()
    lyric_word_thread.join()

if __name__ == "__main__":
    main()
