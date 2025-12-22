FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy config first to cache dependencies (Efficiency Step)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

#2

FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Crucial: This grabs the compiled JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Setup database folder for H2 persistence
RUN mkdir -p /app/data

# Tell the cloud provider which port we use
EXPOSE 8080

# The command to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]