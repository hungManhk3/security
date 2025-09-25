# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.8-amazoncorretto-21 AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 21
FROM amazoncorretto:21.0.4

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# expose port
EXPOSE 8080

# Healthcheck ping vào actuator/health nếu có
HEALTHCHECK --interval=30s --timeout=3s --retries=5 \
  CMD wget -qO- http://127.0.0.1:8080/actuator/health | grep -q '"status":"UP"' || exit 1

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]