import time

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup

from dotenv import load_dotenv
import os

load_dotenv()

chromedriver_path = os.getenv("CHROMEDRIVER_PATH")
options = Options()
options.add_argument('--headless')
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')
options.add_argument("--lang=en-US")


class YoutubeCrawler:
    def __init__(self):
        self.driver = None

    def start_driver(self):
        print("Starting Youtube Crawler")
        self.service = Service(executable_path=chromedriver_path)
        self.driver = webdriver.Chrome(service=self.service, options=options)

    def get_video_info(self, searchWord):
        print("Starting Youtube Crawler : getting video info")
        self.driver.get("https://www.youtube.com/results?search_query=" + searchWord)
        time.sleep(5)
        try:
            print("Starting Youtube Crawler : getting video items")
            video_items = self.driver.find_elements(By.TAG_NAME, "ytd-video-renderer")
            for item in video_items:
                print("Starting Youtube Crawler : getting video"+str(item))
                title_element = item.find_element(By.ID, "video-title")
                title = title_element.get_attribute("title")
                url = title_element.get_attribute("href")
                youtube_id = url.split('v=')[1].split('&')[0]
                aria_label = title_element.get_attribute("aria-label")
                print(aria_label)

                duration = self.extract_duration_from_aria_label(aria_label)
                if duration != "Unknown" and duration != "":
                        print(f"Title: {title}, Duration: {duration}, YouTube ID: {youtube_id}")
                else:
                    self.scroll_down_pixels()
        except Exception as e:
            print(f"Error: {e}")

    def extract_duration_from_aria_label(self, aria_label):
        parts = aria_label.split(" ")
        print(parts[len(parts)-1])

    def scroll_down_pixels(self):
        self.driver.execute_script(f"window.scrollTo(0, 2000 + window.pageYOffset);")
        time.sleep(10)

    def stop_driver(self):
        if self.driver:
            self.driver.quit()
            self.driver = None


crawler = YoutubeCrawler()
crawler.start_driver()
crawler.get_video_info("hello official audio")
crawler.stop_driver()
