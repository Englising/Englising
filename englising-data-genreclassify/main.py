import threading

from core_service.genre_configure_service import GenreConfigureWorker


def main():
    print("build complete")
    genre_configure_worker = GenreConfigureWorker()
    genre_configure_thread = threading.Thread(target=genre_configure_worker.start)
    genre_configure_thread.start()
    genre_configure_thread.join()


if __name__ == "__main__":
    main()
