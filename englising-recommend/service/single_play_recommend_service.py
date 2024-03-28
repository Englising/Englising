import random
from typing import List

import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sqlalchemy import desc

from crud import track_crud
from database.mysql_manager import Session
from model.track import Track
from model.track_like import TrackLike

session = Session()

def get_feature_data(session: Session) -> List[Track]:
    result = track_crud.get_all_tracks(session)
    return result


def get_title_from_track_id(track_id, music_df):
    return music_df[music_df.track_id == track_id]["title"].values[0]

def get_popular_tracks(session, limit=10):
    tracks = session.query(Track.title, Track.spotify_id).order_by(Track.spotify_popularity.desc()).all()
    popular_tracks = tracks[:limit]
    return popular_tracks

def get_user_liked_tracks(session, user_id, limit=3):
    liked_tracks = session.query(TrackLike.track_id).filter(TrackLike.user_id == user_id, TrackLike.is_liked == True).order_by(desc(TrackLike.created_at)).limit(limit).all()
    track_ids = [track_like[0] for track_like in liked_tracks]
    return track_ids

def get_recommend_from_user_id(user_id, limit=18):
    user_likes = get_user_liked_tracks(session, user_id)
    recommendations = get_recommendations(session, user_likes=user_likes, limit=limit)
    return recommendations

def get_recommendations(session, user_likes=[], limit=10):
    music_df = pd.DataFrame(get_feature_data(session))  # music_df 생성
    # print("music_df", music_df)

    if len(user_likes) < 3:
        popular_tracks = get_popular_tracks(session, limit=18)
        return [track[0] for track in popular_tracks]

    features = ['feature_acousticness', 'feature_danceability', 'feature_energy']
    for feature in features:
        music_df[feature] = music_df[feature].fillna('')

    def combine_features(row):
        return str(row['feature_acousticness']) + " " + str(row['feature_danceability']) + " " + str(row['feature_energy'])

    music_df["combine_features"] = music_df.apply(combine_features,axis=1)
    # print("angry!!!!!!!", music_df)

    cv = CountVectorizer()
    count_matrix = cv.fit_transform(music_df["combine_features"])
    cosine_sim = cosine_similarity(count_matrix)

    similar_tracks = []
    for track_id in user_likes:
        if music_df[music_df['track_id'] == track_id].empty:
            continue
        music_user_likes_index = music_df[music_df['track_id'] == track_id].index.values[0]

        similar_music = list(enumerate(cosine_sim[music_user_likes_index]))
        sorted_similar_music = sorted(similar_music, key=lambda x: x[1], reverse=True)[1:]

        for track in sorted_similar_music[:6]:
            track_index = track[0]
            track_id = music_df.iloc[track_index]['track_id']
            similar_tracks.append(track_id)

    unique_similar_tracks = list(set(similar_tracks))
    random.shuffle(unique_similar_tracks)
    return unique_similar_tracks[:limit]
