# Build
FROM maven:3.9.11-eclipse-temurin-24 AS builder
WORKDIR /app/accounting
COPY . /app/accounting
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=builder /app/accounting/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]