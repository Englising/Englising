from client.spotify_client import *


# Ablum Queue로 작업 관리
# 년도별로 앨범 정보들을 가져옴

# spotify_id로 앨범 정보 상세조회
# Artist Queue에 넣음


# Year Album


class AlbumGatherService:
    def __init__(self, redis_host='localhost', redis_port=6379, redis_db=0, channel_name=str(WorkList.ARTIST)):
        self.redis_host = redis_host
        self.redis_port = redis_port
        self.redis_db = redis_db
        self.channel_name = channel_name
        self.redis_connection = redis.Redis(host=self.redis_host, port=self.redis_port, db=self.redis_db)
        self.pubsub = self.redis_connection.pubsub()

    def subscribe_to_channel(self):
        self.pubsub.subscribe(**{self.channel_name: self.handle_message})
        print(f"Subscribed to {self.channel_name}")

        thread = threading.Thread(target=self.pubsub.run_in_thread, kwargs={'sleep_time': 0.001})
        thread.start()
        print("Listening for messages...")

    def get_artist_from_spotify(self, spotify_id):
        print(f"Retrieving artist with Spotify ID: {spotify_id}")

    def handle_message(self, message):
        print(f"Received message: {message}")
        if message['type'] == 'message':
            spotify_id = message['data'].decode('utf-8')
            self.get_artist_from_spotify(spotify_id)

