# Указываем образ с Java 23
FROM openjdk:23-jdk-slim
# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и файл зависимостей для сборки
COPY pom.xml .

# Копируем код приложения
COPY src ./src
RUN apt-get update && apt-get install -y maven
# Собираем Jar файл
RUN mvn clean package -DskipTests

# Копируем скомпилированный Jar файл в образ
COPY target/*.jar app.jar

# Указываем, что контейнер слушает порт 8080
EXPOSE 8080

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
