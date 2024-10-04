# Building the application
FROM maven:3.9.7-sapmachine-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests
RUN echo "done"

# Running the application
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/roomio-backends-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java","-jar","/app/app.jar"]
