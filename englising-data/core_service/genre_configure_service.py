import time
from queue import Queue, Empty
import pandas as pd

from crud.track_crud import *
from database.mysql_manager import Session

from log.log_info import LogList, LogKind
from log.englising_logger import log


class GenreConfigureWorker:
    def __init__(self):
        self.job_queue = Queue()

    def start(self):
        while True:
            session = Session()
            queue_length = self.job_queue.qsize()
            if queue_length <= 10:
                tracks_without_genre = get_tracks_without_genre(session)
                for track in tracks_without_genre:
                    self.job_queue.put(track)
            session.close()

            try:
                track_dto = self.job_queue.get(timeout=5)
                self.process_job(track_dto)
                log(LogList.TRACK.name, LogKind.INFO, "Finished Job: " + str(track_dto.track_id))
                time.sleep(10)
            except Empty:
                time.sleep(10)

    def process_job(self, track: Track):
        log(LogList.TRACK.name, LogKind.INFO, "Starting Job: "+str(track.track_id))
        session = Session()
        try:
            genre = self.get_track_genre(track)
            update_track_genre(session, track, genre)
            session.commit()
        except Exception as e:
            log(LogList.TRACK.name, LogKind.ERROR, str(e))
            self.job_queue.put(track)
            session.rollback()
        finally:
            session.close()

    def get_track_genre(self, track: Track) -> str:
        music_df = pd.DataFrame([{
            'feature_acousticness': track.feature_acousticness,
            'feature_danceability': track.feature_danceability,
            'feature_energy': track.feature_energy
        }])
        music_df = music_df.fillna('')
        music_df['genre'] = music_df.apply(
            lambda row: self.classify_genre(row['feature_acousticness'], row['feature_danceability'],
                                            row['feature_energy']), axis=1)
        return music_df['genre'].iloc[0]

    def classify_genre(self, acousticness, danceability, energy):
        if danceability >= 0.727:
            return 'dance'
        elif acousticness >= 0.171:
            return 'rnb'
        elif energy >= 0.730:
            return 'rock'
        else:
            return 'pop'
