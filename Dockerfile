# 1. Gradle을 사용하여 애플리케이션 빌드
FROM gradle:7.4.0-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
USER gradle
RUN gradle build -x test

# 2. 실행 환경 구성
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
