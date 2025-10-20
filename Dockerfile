# ---- Build stage ----
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -DskipTests clean package

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Minimal runtime packages (curl used by healthcheck)
RUN apk add --no-cache curl

# Copy fat jar
ARG JAR_NAME=target/*-SNAPSHOT.jar
COPY --from=build /src/${JAR_NAME} /app/app.jar

# Entry script reads Swarm secrets if present
COPY docker/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]
