# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built artifact
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Reduced memory settings for cloud deployment
ENTRYPOINT ["java", "-jar", "-Xms128m", "-Xmx256m", "-XX:+UseG1GC", "app.jar"]
