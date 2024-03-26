import time

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from dotenv import load_dotenv
import os

load_dotenv()

chromedriver_path = os.getenv("CHROMEDRIVER_PATH")
options = Options()
options.add_argument('--headless')
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')


class YoutubeCrawler:
    def __init__(self):
        self.driver = None

    def start_driver(self):
        self.service = Service(executable_path=chromedriver_path)
        self.driver = webdriver.Chrome(service=self.service, options=options)

    def get_video_info(self, searchWord):
        self.driver.get("https://www.youtube.com/results?search_query=" + searchWord)
        time.sleep(5)

        try:
            video_items = self.driver.find_elements(By.TAG_NAME, "ytd-video-renderer")
            for item in video_items:
                title_element = item.find_element(By.ID, "video-title")
                title = title_element.get_attribute("title")
                url = title_element.get_attribute("href")
                youtube_id = url.split('v=')[1].split('&')[0]

                duration_elements = item.find_elements(By.CSS_SELECTOR,
                                                       "span.style-scope.ytd-thumbnail-overlay-time-status-renderer")
                duration = duration_elements[0].text.strip() if duration_elements else "Unknown"
                if duration != "Unknown" and duration != "":
                        print(f"Title: {title}, Duration: {duration}, YouTube ID: {youtube_id}")
        except Exception as e:
            print(f"Error: {e}")

    def stop_driver(self):
        if self.driver:
            self.driver.quit()
            self.driver = None


crawler = YoutubeCrawler()
crawler.start_driver()
crawler.get_video_info("blank space (taylor's version) taylor swift official audio")
crawler.stop_driver()
