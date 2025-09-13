FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
COPY src/main/resources/application-prod.properties ./application-prod.properties
EXPOSE 8089
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
