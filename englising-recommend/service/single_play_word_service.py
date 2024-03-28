from typing import List

import nltk
from nltk.corpus import cmudict
from gensim.models import KeyedVectors
import numpy as np
import crud.word_crud as word_crud
import crud.track_crud as track_crud

from database.mysql_manager import Session
from model import TrackWord, Word

nltk.download('cmudict')
cmu_dict = cmudict.dict()

model_path = '../GoogleNews-vectors-negative300.bin.gz'
word_vectors = KeyedVectors.load_word2vec_format(model_path, binary=True)


def recommend_words(all_words:List[TrackWord], level, liked_words:List[Word], recently_played_words:List[Word]):
    # 좋아한 단어와 유사도 측정 (전체 좋아요 단어에 대해 실행, 높을수록 좋음)
    # 단어의 난이도 판단 (난이도에 따라 선택된 난이도와 유사할수록 좋음)
    # 최근 플레이한 단어와 유사도 판단 (전체 플레이한 단어에 대해 실행, 낮을 수록 좋음)
    recommended_words = []

    # 모든 단어에 대한 임베딩을 미리 계산
    # 벡터 임베딩: 단어의 의미를 수치적으로 표현한 다차원 벡터
    word_embeddings = {word.origin_word: get_word_embedding(word.origin_word) for word in all_words}

    # 좋아하는 단어와 최근 플레이한 단어의 임베딩 평균 계산
    liked_embeddings_avg = np.mean([get_word_embedding(word.text) for word in liked_words], axis=0)
    recently_played_embeddings_avg = np.mean([get_word_embedding(word.text) for word in recently_played_words], axis=0)
    for word in all_words:
        # 각 단어에 대한 임베딩
        word_embedding = word_embeddings[word.origin_word]
        # 유사도 계산
        similarity_with_liked = evaluate_cosine_similarity(word_embedding, liked_embeddings_avg)
        similarity_with_recently_played = evaluate_cosine_similarity(word_embedding, recently_played_embeddings_avg)
        # 난이도 계산
        word_difficulty = evaluate_word_complexity(word.origin_word)
        # 조건에 따른 점수 계산 (예시)
        score = similarity_with_liked - similarity_with_recently_played - abs(level - word_difficulty)
        # 점수와 함께 추천 리스트에 추가
        recommended_words.append((word, score))
        # 최종 점수에 따라 정렬
    recommended_words.sort(key=lambda x: x[1], reverse=True)
    # 추천 단어 객체만 반환
    return [word[0] for word in recommended_words]


def get_word_embedding(word: str) -> List[float]:
    try:
        return word_vectors[word]
    except KeyError:
        return None


def evaluate_cosine_similarity(vec1: List[float], vec2: List[float]) -> float:
    # 두 벡터 간의 코사인 유사도를 계산하는 함수 (실제 구현 필요)
    dot_product = np.dot(vec1, vec2)
    norm_vec1 = np.linalg.norm(vec1)
    norm_vec2 = np.linalg.norm(vec2)
    similarity = dot_product / (norm_vec1 * norm_vec2)
    return similarity


def evaluate_word_complexity(word):
    # 기준 syl_count: 단어 음절 수
    # 기준 length_score: 특수 문자 사용 빈도
    # 기준 special_char_score: 단어 길이
    syl_count = __syllable_count__(word)
    length_score = len(word)
    special_char_score = sum(1 for char in word if char in "qzx")
    complexity_score = syl_count + length_score + special_char_score
    return complexity_score


def __syllable_count__(word):
    word = word.lower()
    if word in cmu_dict:
        return [len(list(y for y in x if y[-1].isdigit())) for x in cmu_dict[word]][0]
    else:
        return sum(1 for char in word if char in "aeiouy")


all_words = word_crud.get_all_track_words_by_track_id(Session(), 158)
liked_words = word_crud.get_liked_words_by_user_id(Session(), 1)
recently_played_words = word_crud.get_recently_played_words_by_user_id(Session(), 1)
print("추천 시작")

print(recommend_words(all_words,1, liked_words, recently_played_words))
