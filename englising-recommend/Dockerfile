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

# FastText 모델 다운로드 및 압축 해제
RUN wget https://dl.fbaipublicfiles.com/fasttext/vectors-english/wiki-news-300d-1M-subword.vec.zip \
    && mkdir -p english-recommend/word_model \
    && unzip wiki-news-300d-1M-subword.vec.zip -d english-recommend/word_model \
    && rm wiki-news-300d-1M-subword.vec.zip

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8083"]
