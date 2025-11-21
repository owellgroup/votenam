# Multi-stage build for smaller final image

# Stage 1: Build the application
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Copy Maven files
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copy source code
COPY src ./src

# Build the application
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests

# Stage 2: Runtime image
FROM openjdk:21-jre-slim

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/votenam-0.0.1-SNAPSHOT.jar app.jar

# Create uploads directory with proper permissions
RUN mkdir -p /app/uploads/photos /app/uploads/partylogo && \
    chmod -R 755 /app/uploads && \
    chown -R 1000:1000 /app/uploads

# Create a non-root user for security
RUN useradd -m -u 1000 appuser && \
    chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port 8484
EXPOSE 8484

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8484/api/voter || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

