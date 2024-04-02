import logging
from logging.handlers import RotatingFileHandler
from log.log_format import CustomFormatter

log_file_path = "C:\\Users\\SSAFY\\Desktop\\logs.log"

# Logger Create
logger = logging.getLogger("Spotify")

# Logger Levels
logger.setLevel(logging.DEBUG)

# 파일 핸들러 설정
# file_handler = RotatingFileHandler(log_file_path, maxBytes=10485760, backupCount=5, encoding="utf-8")
# file_handler.setLevel(logging.DEBUG)
# file_handler.setFormatter(CustomFormatter())

# 콘솔 핸들러 설정
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.DEBUG)
console_handler.setFormatter(CustomFormatter())

# 핸들러를 로거에 추가
# logger.addHandler(file_handler)
logger.addHandler(console_handler)
