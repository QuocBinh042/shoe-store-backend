FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-Xmx256m", "-Dserver.port=${PORT}", "-jar", "app.jar"]