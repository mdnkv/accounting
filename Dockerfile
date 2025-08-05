FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]