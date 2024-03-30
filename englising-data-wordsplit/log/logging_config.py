import logging
from log.log_format import CustomFormatter

# Logger Create
logger = logging.getLogger("Spotify")

# Logger Levels
logger.setLevel(logging.DEBUG)

# 콘솔 핸들러 설정
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)
console_handler.setFormatter(CustomFormatter())

# 핸들러를 로거에 추가
logger.addHandler(console_handler)
