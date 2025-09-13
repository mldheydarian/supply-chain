FROM eclipse-temurin:17-jdk-alpine AS build
RUN apk add --no-cache bash git
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests -Pprod

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/supply-chain.jar
COPY ./src/main/resources/application-prod.properties /app/
COPY ./src/main/resources/application.yml /app/

EXPOSE 8090

CMD ["java", "-jar", "supply-chain.jar", "--spring.config.location=/app/application-prod.properties"]
