import redis
import threading
from util.worklist import WorkList


class ArtistQueueManager:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, channel_name=WorkList.ARTIST):
        self.redis_host = redis_host
        self.redis_port = redis_port
        self.redis_db = redis_db
        self.channel_name = channel_name
        self.redis_connection = redis.Redis(host=self.redis_host, port=self.redis_port, db=self.redis_db)
        self.pubsub = self.redis_connection.pubsub()

    def subscribe_to_channel(self):
        self.pubsub.subscribe(channel_name=self.handle_message)
        print(f"Subscribed to {self.channel_name}")

        thread = threading.Thread(target=self.pubsub.run_in_thread, kwargs={'sleep_time': 0.001})
        thread.start()
        print("Listening for messages...")

    def get_artist_from_spotify(self, spotify_id):
        # 여기에 Spotify에서 아티스트를 조회하는 로직을 구현하세요.
        print(f"Retrieving artist with Spotify ID: {spotify_id}")

    def handle_message(self, message):
        if message['type'] == 'message':
            spotify_id = message['data'].decode('utf-8')
            self.get_artist_from_spotify(spotify_id)



# 사용 예시
if __name__ == "__main__":
    manager = ArtistQueueManager()
    manager.subscribe_to_channel()
