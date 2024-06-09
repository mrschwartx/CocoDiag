FROM python:3.11-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libsm6 \
    libxext6 \
    libxrender1 \
    firefox-esr \
    wget \
    curl \
    unzip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

RUN GECKODRIVER_VERSION=0.34.0 && \
    wget -q --show-progress --progress=bar:force:noscroll -O /tmp/geckodriver.tar.gz "https://github.com/mozilla/geckodriver/releases/download/v${GECKODRIVER_VERSION}/geckodriver-v${GECKODRIVER_VERSION}-linux64.tar.gz" && \
    tar -C /usr/local/bin -zxf /tmp/geckodriver.tar.gz && \
    rm /tmp/geckodriver.tar.gz

ENV DISPLAY=:99

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . /app

EXPOSE 8080

CMD ["gunicorn", "-b", ":8080", "main:app"]