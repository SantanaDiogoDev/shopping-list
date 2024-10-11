# Etapa de construção
FROM gradle:8.10.1-jdk17 AS build
WORKDIR /app
COPY . /app/
RUN gradle build --no-daemon -x test --no-build-cache

# Etapa de execução
FROM openjdk:17-jdk-alpine
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
