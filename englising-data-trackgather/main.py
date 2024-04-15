
import threading
from core_service.track_gather_service import TrackWorker


def main():
    print("build complete")
    track_worker = TrackWorker()
    track_thread = threading.Thread(target=track_worker.start)
    track_thread.start()
    track_thread.join()


if __name__ == "__main__":
    main()
