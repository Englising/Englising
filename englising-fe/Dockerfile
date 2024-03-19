FROM node:alpine as build

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# React 애플리케이션을 빌드
RUN npm run build
# Nginx 이미지
FROM nginx:alpine
# Nginx의 기본 설정을 제거, 커스텀 설정 파일을 복사
# React Router를 사용하는 SPA의 경우 설정 필요
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
# 애플리케이션의 빌드 결과물 Nginx가 호스팅할 위치로 복사
COPY --from=build /app/dist /usr/share/nginx/html
# 80 포트를 노출
EXPOSE 5173
# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
