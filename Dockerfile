FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ENV APP_DB_FILE_PATH=/app/data/database
ENV APP_ACCESS_LOG_DIR=/app/data/logs

VOLUME ["/app/data"]

COPY --from=build /app/target/manager-0.0.1-SNAPSHOT.jar /app/manager.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "manager.jar"]
