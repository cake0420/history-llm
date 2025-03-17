# 1. OpenJDK 17 이미지에서 시작
FROM openjdk:17-jdk-slim as build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Spring Boot 애플리케이션 소스 파일을 Docker 컨테이너로 복사
COPY . /app

# 4. Maven을 사용하여 애플리케이션 빌드 (pom.xml이 프로젝트 루트에 있어야 함)
RUN ./mvnw clean package -DskipTests

# 5. 실제 애플리케이션을 실행할 경로로 설정
FROM openjdk:17-jdk-slim

WORKDIR /app

# 6. 빌드한 jar 파일을 복사 (최종 단계로 사용)
COPY --from=build /app/target/*.jar app.jar

# 7. 컨테이너가 시작될 때 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 8. 애플리케이션이 사용하는 포트를 열기
EXPOSE 8080
