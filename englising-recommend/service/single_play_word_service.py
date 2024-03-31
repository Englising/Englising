import random
from typing import List

import numpy as np
from database.mysql_manager import get_session

import crud.word_crud as word_crud
from config.fast_text_config import fast_text_model
from model import TrackWord, Word


def get_recommended_track_words(user_id: int, track_id: int, level: int) -> List[TrackWord]:
    with get_session() as session:
        all_words = word_crud.get_all_track_words_by_track_id(session, track_id)
        liked_words = word_crud.get_liked_words_by_user_id(session, user_id)
        recently_played_words = word_crud.get_recently_played_words_by_user_id(session, user_id)
        recommended_words = __recommend_words__(all_words, level, liked_words, recently_played_words)
    final_selected_words = __select_words_from_recommended__(level, recommended_words)
    return final_selected_words


def __recommend_words__(all_track_words: List[TrackWord], level, liked_words: List[Word], recently_played_words: List[Word]) -> List[TrackWord]:
    # 좋아한 단어와 유사도 판단
    # 단어의 난이도 판단
    # 최근 플레이 한 단어와 유사도 판단, 유사하지 않도록 함
    played_avg_vector = np.mean([__get_word_vector__(word.en_text) for word in recently_played_words], axis=0)
    played_avg_scalar = np.mean(played_avg_vector)
    liked_avg_vector = np.mean([__get_word_vector__(word.en_text) for word in liked_words], axis=0)
    liked_avg_scalar = np.mean(liked_avg_vector)
    recommended_words = []
    for word in all_track_words:
        word_vector = __get_word_vector__(word.origin_word)
        word_scalar = np.mean(word_vector)

        score = (1 - __cosine_similarity__(word_vector, played_avg_scalar)) + __cosine_similarity__(word_vector, liked_avg_scalar)
        difficulty_socre = 1 - abs(__evaluate_difficulty__(word.origin_word) - level)
        total_score = score + difficulty_socre
        recommended_words.append((word, total_score))

    recommended_words.sort(key=lambda x: x[1], reverse=True)
    return recommended_words


def __select_words_from_recommended__(level, recommended_words):
    word_count = {1: 30, 2: 40, 3: 50}
    final_count = word_count.get(level, 30)

    seen_words = set()
    unique_recommended_words = []
    for word, score in recommended_words:
        if word.origin_word not in seen_words:
            seen_words.add(word.origin_word)
            unique_recommended_words.append((word, score))
    if len(unique_recommended_words) < final_count*2:
        for word, score in recommended_words:
            already_in = False
            for word_selected, _ in unique_recommended_words:
                if word_selected.track_word_id == word.track_word_id:
                    already_in = True
                    break
            if not already_in:
                unique_recommended_words.append((word, score))
            if len(unique_recommended_words) >= len(recommended_words):
                break
    random.shuffle(unique_recommended_words)
    final_recommendations = unique_recommended_words[:final_count]
    return [word for word, score in final_recommendations]


def __get_word_vector__(word: str):
    return np.mean(fast_text_model[word], axis=0)


def __cosine_similarity__(vec1, vec2):
    dot_product = np.dot(vec1, vec2)
    norm_vec1 = np.linalg.norm(vec1)
    norm_vec2 = np.linalg.norm(vec2)
    if norm_vec1 == 0 or norm_vec2 == 0:
        return 0
    cosine_similarity = dot_product / (norm_vec1 * norm_vec2)
    return cosine_similarity


def __evaluate_difficulty__(word: str):
    # 단어 난이도 평가, 1, 2, 3 중 하나의 난이도 등급 반환
    # 반환값: 1 (쉬움), 2 (중간), 3 (어려움)
    length_score = len(word)
    syllable_count = sum(1 for char in word if char in "aeiouy")  # 간단한 음절 수 계산
    special_char_score = sum(1 for char in word if not char.isalnum())  # 특수 문자 수

    # 단어 길이, 음절 수, 특수 문자 수를 기반 난이도 점수 계산
    difficulty_score = length_score + syllable_count + special_char_score

    # 난이도 점수를 기반 난이도 등급 할당
    if difficulty_score <= 5:
        return 1
    elif difficulty_score <= 8:
        return 2
    else:
        return 3
