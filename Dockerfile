FROM openjdk:11 as builder

COPY . .

RUN chmod 755 ./gradlew && \
    ./gradlew bootjar


FROM openjdk:11 as runner

WORKDIR /app

COPY --from=builder ./server/build/libs/*.jar app.jar

EXPOSE 8080


ENTRYPOINT java -jar -Djasypt.encryptor.password=${TOPPINGS_KEY} -Dspring.profiles.active=prod app.jar -Duser.timezone=Asia/Seoul
