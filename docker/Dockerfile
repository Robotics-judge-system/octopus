# syntax=docker/dockerfile:1
FROM openjdk:16-alpine3.13

WORKDIR /app

COPY ./octopus.jar ./application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./application.jar"]
