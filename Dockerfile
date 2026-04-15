# syntax=docker/dockerfile:1
# Imagen de producción del backend Spring Boot (Java 21)

FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar /app/app.jar
RUN chown spring:spring /app/app.jar \
    && mkdir -p /app/uploads \
    && chown spring:spring /app/uploads

USER spring:spring

EXPOSE 8085

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
