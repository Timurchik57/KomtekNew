FROM openjdk:17-alpine
RUN apk update && apk add --no-cache maven

# Копируем локальный архив Allure в директорию /app
COPY /program/allure-2.34.1 /bin/allure

WORKDIR /app
COPY . /app

# чтобы компиляция не проходила во время сборки контейнера
RUN mvn clean install -Dmaven.test.skip=true

ENV TestRun ''
CMD $TestRun

#COPY entrypoint.sh /app/entrypoint.sh
#RUN chmod +x /app/entrypoint.sh
#ENTRYPOINT ["/app/entrypoint.sh"]

