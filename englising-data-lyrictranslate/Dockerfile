FROM python:3.10.11

WORKDIR /app
COPY . /app

COPY requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

CMD ["python", "main.py"]
