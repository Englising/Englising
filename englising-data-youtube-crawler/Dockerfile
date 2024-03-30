# Python 3.10 공식 이미지를 사용
FROM python:3.10

# 작업 디렉토리 설정
WORKDIR /app

# Python 의존성 파일 복사
COPY requirements.txt ./

# pip를 사용하여 requirements.txt에 명시된 패키지들을 설치
RUN pip install --no-cache-dir -r requirements.txt

# Chrome 설치를 위한 필요 패키지 설치
RUN apt-get update && \
    apt-get install -y wget gnupg2 unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Google Chrome 설치
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Chrome 버전 확인
RUN google-chrome --version | grep -oE "[0-9.]+" | head -1 > /chrome-version.txt

# ChromeDriver 설치 (Chrome 버전에 맞게 자동으로 선택)
RUN CHROME_VERSION=$(cat /chrome-version.txt) && \
    CHROMEDRIVER_VERSION=$(wget -qO- "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION") && \
    wget -O /tmp/chromedriver.zip "http://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip" && \
    unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip && \
    chmod +x /usr/local/bin/chromedriver

# 애플리케이션 파일 복사
COPY . .

# 애플리케이션 실행
CMD ["python", "./main.py"]
