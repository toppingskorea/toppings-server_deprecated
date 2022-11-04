FROM openjdk:11 as builder

COPY . .

RUN chmod 755 ./gradlew && \
    ./gradlew bootjar


FROM openjdk:11 as runner

WORKDIR /app

ARG TOPPINGS_KEY

COPY --from=builder ./server/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT java -jar -Djasypt.encryptor.password=${TOPPINGS_KEY} -Dspring.profiles.active=dev app.jar -Duser.timezone=Asia/Seoul
