import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

from dto.word_dto import WordDto

chromedriver_path = "../chromedriver.exe"


class NaverDictionaryScraper:
    def __init__(self):
        self.driver = None

    def start_driver(self):
        self.service = Service(executable_path=chromedriver_path)
        self.driver = webdriver.Chrome(service=self.service)

    def get_word_details(self, word: str) -> WordDto:
        url = f'https://en.dict.naver.com/#/search?query={word}&range=all'
        self.driver.get(url)
        time.sleep(1)
        wordDto = WordDto()
        wordDto.en_text = word
        meanings = self.fetch_meanings()
        wordDto.ko_text_1 = meanings[0] if len(meanings) > 0 else ''
        wordDto.ko_text_2 = meanings[1] if len(meanings) > 1 else ''
        wordDto.ko_text_3 = meanings[2] if len(meanings) > 2 else ''
        wordDto.example = self.fetch_examples()
        return wordDto

    def fetch_examples(self) -> str:
        example_element = self.driver.find_element(By.CSS_SELECTOR, "div.component_example.has-saving-function .row div.origin.is-audible span.text")
        english_example = example_element.text.strip()
        return english_example

    def fetch_meanings(self) -> list:
        meanings_elements = self.driver.find_elements(By.CSS_SELECTOR, "ul.mean_list.multi li.mean_item p.mean")
        meanings = [element.text.strip() for element in meanings_elements]
        return meanings

    def stop_driver(self):
        if self.driver:
            self.driver.quit()
            self.driver = None
