FROM python:3.10

# 작업 디렉토리 설정
WORKDIR /app

# 앱 파일 복사
COPY . /app

# 필요한 패키지 설치
RUN apt-get update \
    && apt-get install -y wget unzip \
    && pip install --no-cache-dir -r requirements.txt

# NLTK 데이터 다운로드
RUN python -m nltk.downloader punkt averaged_perceptron_tagger wordnet stopwords


CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8083"]
