from model.track_word import TrackWord


def create_track_word(track_word: TrackWord, session):
    session.add(track_word)
    session.flush()
    session.refresh(track_word)
    return track_word


