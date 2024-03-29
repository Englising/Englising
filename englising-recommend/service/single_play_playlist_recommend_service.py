import random

import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

from crud import track_crud as track_crud
from database.mysql_manager import Session


def get_recommend_for_user_id(user_id, limit=18):
    user_likes = track_crud.get_user_liked_track_ids(Session(), user_id)
    user_played = track_crud.get_user_played_tracks(Session(), user_id)
    if user_likes is None or len(user_likes) == 0:
        return __get_popular_tracks__()
    else:
        return __get_recommended_tracks__(user_likes, user_played, limit)


def __get_popular_tracks__(limit=18):
    popular_tracks = track_crud.get_tracks_by_single_played_count_and_spotify_popularity(Session(), 60)
    popular_track_ids = [track.track_id for track in popular_tracks]
    return random.sample(popular_track_ids, limit)


def __get_recommended_tracks__(user_likes=[], user_played=[], limit=18):
    # Track Model로 DataFrame 생성
    tracks = track_crud.get_all_tracks(Session())
    track_data = [{
        'track_id': track.track_id,
        'feature_acousticness': track.feature_acousticness,
        'feature_danceability': track.feature_danceability,
        'feature_energy': track.feature_energy
    } for track in tracks]
    # 추출된 데이터로부터 DataFrame 생성
    music_df = pd.DataFrame(track_data)
    # None인 Track Feature를 ''로 입력
    features = ['feature_acousticness', 'feature_danceability', 'feature_energy']
    for feature in features:
        music_df[feature] = music_df[feature].fillna('')
    # Feature들을 공백을 사이에 두고 String으로 결합, DataFrame에 새로운 열 추가
    music_df["combine_features"] = music_df.apply(
        lambda row: f"{row['feature_acousticness']} {row['feature_danceability']} {row['feature_energy']}", axis=1)
    # Feature 문자열을 벡터로 변환
    cv = CountVectorizer()
    count_matrix = cv.fit_transform(music_df["combine_features"])
    # 벡터 기반으로 코사인 유사도 계산
    cosine_sim = cosine_similarity(count_matrix)
    # 추천 플레이리스트 TrackId 배열 생성
    recommend_per_track = 50/len(user_likes) + 1
    recomended_track_ids = []
    for liked_track_id in user_likes:
        # DataFrame에 liked_track_id가 있는지 확인
        if music_df[music_df['track_id'] == liked_track_id].empty:
            continue
        # liked_track_id의 Track 인덱스 찾기
        music_user_likes_index = music_df[music_df['track_id'] == liked_track_id].index.values[0]
        # 유사도 계산 결과 튜플 리스트로 변환
        similar_music = list(enumerate(cosine_sim[music_user_likes_index]))
        # 유사도 기준 정렬
        sorted_similar_music = sorted(similar_music, key=lambda x: x[1], reverse=True)[1:]
        # User가 좋아요 한 노래들을 순회하며 유사도가 높은 순으로 특정 개수만큼 추출
        current_recommendated_tracks = 0
        for track in sorted_similar_music:
            track_index = track[0]
            track_id = music_df.iloc[track_index]['track_id']
            if (not any(track.track_id == track_id for track in user_played)
                and track_id not in user_likes
                and track_id not in recomended_track_ids):
                recomended_track_ids.append(track_id)
                current_recommendated_tracks += 1
            if current_recommendated_tracks == recommend_per_track:
                break
    random.shuffle(recomended_track_ids)
    return recomended_track_ids[:limit]
