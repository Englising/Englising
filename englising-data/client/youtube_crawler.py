import time

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By

from dotenv import load_dotenv
import os

from dto.track_dto import YoutubeDto
from log.log_info import LogList, LogKind
from log.englising_logger import log

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
        self.service = Service(executable_path=chromedriver_path)
        self.driver = webdriver.Chrome(service=self.service, options=options)

    def get_video_info(self, track_title, artist_name, duration_ms) -> YoutubeDto:
        log(LogList.YOUTUBE.name, LogKind.INFO, f"Searching {track_title} {artist_name}")
        self.driver.get("https://www.youtube.com/results?search_query=" + f"{track_title} {artist_name} official audio")
        time.sleep(10)
        youtube_list = []
        try:
            video_items = self.driver.find_elements(By.TAG_NAME, "ytd-video-renderer")
            for item in video_items:
                if len(youtube_list) == 10:
                    break
                title_element = item.find_element(By.ID, "video-title")
                title = title_element.get_attribute("title")
                url = title_element.get_attribute("href")
                youtube_id = url.split('v=')[1].split('&')[0]
                aria_label = title_element.get_attribute("aria-label")
                duration = self.extract_duration_from_aria_label(aria_label)
                if duration != "Unknown" and duration != "":
                    youtube_list.append(YoutubeDto(
                        youtube_id = youtube_id,
                        duration_ms = duration
                    ))
                else:
                    break
        except Exception as e:
            log(LogList.YOUTUBE.name, LogKind.ERROR, f"Failed Searching {track_title} {artist_name} {e}")
        finally:
            log(LogList.YOUTUBE.name, LogKind.INFO, f"Finished Searching {track_title} {artist_name}")
            return self.figure_closest_time(youtube_list, duration_ms)


    def extract_duration_from_aria_label(self, aria_label):
        parts = aria_label.split(" ")
        hours, minutes, seconds = 0, 0, 0
        for part in parts:
            if "시간" in part:
                hours = int(part.split("시간")[0])
                time_str = part.split("시간")[1].strip()
            if "분" in part:
                minutes = int(part.split("분")[0])
                time_str = part.split("분")[1].strip()
            if "초" in part:
                seconds = int(part.split("초")[0])
        total_ms = (hours * 3600 + minutes * 60 + seconds) * 1000
        return total_ms

    def figure_closest_time(self, youtube_list, duration_ms) -> YoutubeDto:
        closest_youtube = None
        closest_time = None
        for youtube in youtube_list:
            diff = abs(youtube.duration_ms - duration_ms)
            if closest_time is None or diff < closest_time:
                closest_youtube = youtube
                closest_time = diff
        if closest_time > 1000:
            log(LogList.YOUTUBE.name, LogKind.WARNING,
                f"Can't figure out Close video closest={closest_time}, duration={duration_ms}")
            return None
        return closest_youtube

    def stop_driver(self):
        if self.driver:
            self.driver.quit()
            self.driver = None

