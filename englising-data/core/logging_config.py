import logging
from logging.handlers import RotatingFileHandler

# 기본 로그 설정
log_level = logging.INFO
log_file_path = "C:\\Users\\SSAFY\\Desktop"

# 로거 생성 및 로그 레벨 설정
logger = logging.getLogger("Logger")
logger.setLevel(log_level)

# 로그 포맷 정의
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

# 파일 핸들러 설정
file_handler = RotatingFileHandler(log_file_path, maxBytes=10485760, backupCount=5)
file_handler.setLevel(log_level)
file_handler.setFormatter(formatter)

# 콘솔 핸들러 설정
console_handler = logging.StreamHandler()
console_handler.setLevel(log_level)
console_handler.setFormatter(formatter)

# 핸들러를 로거에 추가
logger.addHandler(file_handler)
logger.addHandler(console_handler)