FROM gradle:jdk17-alpine AS build

WORKDIR /app

COPY build.gradle.kts .
COPY gradlew .
COPY gradle gradle
COPY src src

##RUN ./gradlew test -P spring.profiles.active=dev
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre-alpine AS run

WORKDIR /app

COPY --from=build /app/build/libs/*SNAPSHOT.jar /app/my-app.jar


EXPOSE 3000


ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app/my-app.jar"]