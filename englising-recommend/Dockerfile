FROM python:3.10

WORKDIR /app

COPY . /app

RUN pip install --no-cache-dir -r requirements.txt
RUN python -m nltk.downloader punkt averaged_perceptron_tagger wordnet stopwords

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8083"]
