import time

import redis
import threading
from core_service.artist_gather_service import ArtistQueueManager


# ArtistQueueManager 인스턴스 생성
aqm = ArtistQueueManager()

# 채널에 구독 시작
aqm.subscribe_to_channel()

# 테스트 메시지 발행을 위한 함수 정의
def publish_test_messages(channel_name, messages):
    redis_connection = redis.Redis(host='localhost', port=6379, db=0)
    for msg in messages:
        redis_connection.publish(channel_name, msg)
        print(f"Published message: {msg}")


# 별도의 스레드에서 테스트 메시지 발행 시작
test_messages = ['test_spotify_id_1', 'test_spotify_id_2']
thread = threading.Thread(target=publish_test_messages, args=(aqm.channel_name, test_messages))
time.sleep(5)
thread.start()
