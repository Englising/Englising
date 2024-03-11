import redis
from util.worklist import WorkList

# 레디스 서버 연결
redis_client = redis.Redis(host='localhost', port=6379, db=0)

for enum in WorkList :
    redis_client.rpush(str(enum), str(enum))
