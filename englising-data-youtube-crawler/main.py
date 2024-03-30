import time

import threading

from core_service.youtube_crawler_service import YoutubeWorker


def main():
    youtube_worker = YoutubeWorker()
    youtube_thread = threading.Thread(target=youtube_worker.start)
    youtube_thread.start()
    youtube_thread.join()


if __name__ == "__main__":
    main()
