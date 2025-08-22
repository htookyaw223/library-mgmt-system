# Use a secure and official base image with Java 17
FROM eclipse-temurin:17-jdk-jammy

# Argument to hold the name of the JAR file
ARG JAR_FILE=target/*.jar

# Set a working directory inside the container
WORKDIR /opt/app

# Copy the JAR file from our build context to the container
COPY ${JAR_FILE} app.jar

# Expose the port the app runs on (Spring Boot default is 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]