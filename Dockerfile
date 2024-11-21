FROM maven:3.9.9-eclipse-temurin-21-jammy

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "com.example.demo1.0.0.1-SNAPSHOT.jar"]

