FROM maven:3.8.4-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jre-slim

WORKDIR /app

ENV APP_DB_FILE_PATH=/app/data/database
ENV APP_ACCESS_LOG_DIR=/app/logs/

COPY --from=build /app/target/manager-0.0.1-SNAPSHOT.jar /app/manager.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]