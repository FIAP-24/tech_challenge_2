# Construindo pacote
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Criando imagem jdk 21 slim
FROM openjdk:21-slim
WORKDIR /app

# Copiando o JAR que foi criado no builder stage
COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3308/challenge
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=Test@123


# Iniciando app
CMD ["java", "-jar", "app.jar"]