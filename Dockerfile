# Estágio 1: Build
FROM maven:3.9.9-eclipse-temurin-17-focal AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]