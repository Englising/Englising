import time
import re
from queue import Queue, Empty
from typing import List

import nltk
from nltk.corpus import wordnet as wn
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from nltk import pos_tag

from database.mysql_manager import Session
from client.naver_dict_client import NaverDictionaryScraper
from crud.word_crud import create_word, find_word_by_en_text
from crud.lyric_crud import get_lyric_dtos_by_track_id
from crud.track_word_crud import *
from dto.lyric_dto import LyricDto
from model.word import Word
from log.log_info import LogList, LogKind
from log.englising_logger import log

nltk.download('wordnet')
nltk.download('averaged_perceptron_tagger')
nltk.download('punkt')
nltk.download('stopwords')


class LyricWordWorker:
    def __init__(self):
        self.job_queue = Queue()
        self.naver_scraper = NaverDictionaryScraper()
        self.stop_words = set(stopwords.words('english'))
        self.lemmatizer = WordNetLemmatizer()

    def start(self):
        while True:
            # 만약 queue에 남은 데이터가 얼마 없으면 단어가 없는 Lyric을 조회해서 가져온다
            # queue에는 Lyric을 넣음
            if self.job_queue.qsize() <= 10:
                session = Session()
                self.job_queue.put(get_lyric_dtos_by_track_id(158, session))
            try:
                lyric_list = self.job_queue.get(timeout=5)
                self.process_job(lyric_list)
                log(LogList.LYRIC_WORD.name, LogKind.INFO, f"Finished processing lyric data: {lyric_list}")
                return
                time.sleep(1)
            except Empty:
                log(LogList.LYRIC_WORD.name, LogKind.INFO, "Queue is empty, waiting...")
                time.sleep(10)

    def process_job(self, lyric_list: List[LyricDto]):
        session = Session()
        for lyric in lyric_list:
            print(lyric.lyric_id)
            print(lyric.en_text)
        try:
            self.naver_scraper.start_driver()
            # 가사 한 줄 처리
            for lyric in lyric_list:
                # 단어로 가사 분리
                # origin_words = self.extract_words_from_lyric(lyric.en_text)
                print("lyric"+str(lyric.lyric_id))
                origin_words = self.extract_words_from_lyric_with_space(lyric.en_text)
                print(origin_words)
                for index, refined_word, origin_word in origin_words:
                    # 단어 하나씩 Word Table에 저장
                    word = find_word_by_en_text(refined_word, session)
                    print("entext "+word.en_text)
                    if word is None:
                        word_dto = self.naver_scraper.get_word_details(refined_word)
                        if word_dto.ko_text_1 is None or word_dto.ko_text_1 == '':
                            continue
                        word = self.save_word_details(word_dto, session)
                    # TrackWord Table에 저장
                    create_track_word(TrackWord(
                        track_id = lyric.track_id,
                        lyric_id = lyric.lyric_id,
                        word_id = word.word_id,
                        word_index = index,
                        origin_word = origin_word
                    ), session)
                    print(("saved word"))
            session.commit()
        except Exception as e:
            log(LogList.LYRIC_WORD.name, LogKind.ERROR, f"Error processing lyric data: {e}")
            self.job_queue.put(lyric_list)
            session.rollback()
            time.sleep(1)
        finally:
            self.naver_scraper.stop_driver()
            session.close()

    def extract_words_from_lyric(self, en_text) -> list:
        words = word_tokenize(en_text)
        tagged_words = pos_tag(words)

        filtered_words_with_index = [
            (index, self.lemmatizer.lemmatize(word.lower(), self.get_wordnet_pos(tag)), word.lower())
            for index, (word, tag) in enumerate(tagged_words)
            if word.isalpha() and word.lower() not in self.stop_words and len(word) > 2 and self.is_wordnet_word(word)
        ]
        return filtered_words_with_index

    def extract_words_from_lyric_with_space(self, en_text) -> list:
        splitted_words = en_text.split(" ")
        index = -1
        finished_words = []

        for word in splitted_words:
            index += 1
            tokenized_words = word_tokenize(word)
            tagged_words = pos_tag(tokenized_words)
            for word, tag in tagged_words:
                if word.isalpha() and word.lower() not in self.stop_words and len(word) > 2 and self.is_wordnet_word(word):
                    finished_words.append((index, self.lemmatizer.lemmatize(word.lower(), self.get_wordnet_pos(tag)), word.lower()))
                index += 1
        return finished_words

    def save_word_details(self, word_dto, session) -> Word:
        word = Word(
            en_text = word_dto.en_text,
            ko_text_1 = word_dto.ko_text_1,
            ko_text_2 = word_dto.ko_text_2,
            ko_text_3 = word_dto.ko_text_3,
            example = word_dto.example
        )
        return create_word(word, session)

    def get_wordnet_pos(self, treebank_tag):
        if treebank_tag.startswith('J'):
            return wn.ADJ
        elif treebank_tag.startswith('V'):
            return wn.VERB
        elif treebank_tag.startswith('N'):
            return wn.NOUN
        elif treebank_tag.startswith('R'):
            return wn.ADV
        else:
            return wn.NOUN

    def is_wordnet_word(self, word):
        return bool(wn.synsets(word))


if __name__ == "__main__":
    worker = LyricWordWorker()
    worker.start()
