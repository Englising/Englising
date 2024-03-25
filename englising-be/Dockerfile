FROM docker
COPY --from=docker/buildx-bin:latest /buildx /usr/libexec/docker/cli-plugins/docker-buildx

FROM openjdk:17-slim
EXPOSE 443

# Redis CLI 설치를 위한 명령어를 추가합니다.
# Debian 또는 Ubuntu 기반의 openjdk 이미지를 가정합니다.
RUN apt-get update && apt-get install -y redis-tools

ADD ./build/libs/englising-be-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
