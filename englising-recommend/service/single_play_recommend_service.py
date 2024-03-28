import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sqlalchemy import desc

from database.mysql_manager import Session
from model.track import Track
from model.track_like import TrackLike

session = Session()

def get_feature_data(session):
    result = session.query(Track.title, Track.track_id, Track.spotify_id, Track.spotify_popularity, Track.feature_acousticness, Track.feature_danceability, Track.feature_energy).all()
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

def get_recommend_from_user_id(user_id, limit=10):
    user_likes = get_user_liked_tracks(session, user_id)
    recommendations = get_recommendations(session, user_likes=user_likes, limit=limit)
    return recommendations

def get_recommendations(session, user_likes=[], limit=10):
    music_df = pd.DataFrame(get_feature_data(session))  # music_df 생성

    if len(user_likes) < 3:
        popular_tracks = get_popular_tracks(session, limit=limit)
        return [track[0] for track in popular_tracks]

    features = ['feature_acousticness', 'feature_danceability', 'feature_energy']
    for feature in features:
        music_df[feature] = music_df[feature].fillna('')

    def combine_features(row):
        try:
            # print(row['feature_acousticness'], row['feature_danceability'], row['feature_energy'])
            return str(row['feature_acousticness']) + " " + str(row['feature_danceability']) + " " + str(row['feature_energy'])
        except:
            print("Error: ", music_df)

    music_df["combine_features"] = music_df.apply(combine_features,axis=1)

    cv = CountVectorizer()
    count_matrix = cv.fit_transform(music_df["combine_features"])
    cosine_sim = cosine_similarity(count_matrix)

    similar_tracks = []
    for track_id in user_likes:
        music_user_likes_title = get_title_from_track_id(track_id, music_df)
        print("사용자가 좋아하는 노래:", music_user_likes_title)

        music_user_likes_index = music_df[music_df['track_id'] == track_id].index.values[0]
        similar_music = list(enumerate(cosine_sim[music_user_likes_index]))
        sorted_similar_music = sorted(similar_music, key=lambda x: x[1], reverse=True)[1:]

        for track in sorted_similar_music[:5]:
            track_index = track[0]
            track_id = music_df.iloc[track_index]['track_id']
            similar_tracks.append(track_id)

    unique_similar_tracks = list(set(similar_tracks))
    return unique_similar_tracks[:limit]

# 사용자의 ID가 user_id인 경우
user_id = 1  # 사용자의 ID에 맞게 수정
user_likes = get_user_liked_tracks(session, user_id)
print("사용자가 좋아하는 노래!!!!!!!!!!", user_likes)

recommendations = get_recommendations(session, user_likes=user_likes, limit=10)
print("추천하는 유사한 노래:")
for track in recommendations:
    print(track)
