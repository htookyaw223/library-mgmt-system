# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven wrapper and prepare dependencies
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source and build the jar
COPY src ./src
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/library-management-system.jar library-management-system.jar

# Default run (can override with SPRING_PROFILES_ACTIVE)
ENTRYPOINT ["java", "-jar", "library-management-system.jar"]
