# 빌드 스테이지
FROM gradle:7.4.2-jdk17 AS build

WORKDIR /app

# Gradle Wrapper와 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 의존성 설치
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY . .

# Gradle 빌드 (테스트 제외)
RUN ./gradlew build -x test --no-daemon

# 실행 스테이지
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 컨테이너 시작 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
