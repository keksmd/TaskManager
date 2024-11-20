# Use the official Maven image with OpenJDK 21
FROM maven:3.9.9-eclipse-temurin-21-jammy

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests &&  echo "$(ls -la /app/target)"
# Copy the packaged JAR file from the builder stage
# Command to run the application
ENTRYPOINT ["java", "-jar", "com.example.demo1.0.0.1-SNAPSHOT.jar"]

