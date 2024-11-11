# Вказуємо базовий образ для Java
FROM openjdk:21-jdk-slim

# Додаємо аргумент для визначення jar-файлу
ARG JAR_FILE=target/*.jar

# Копіюємо зібраний jar-файл в контейнер
COPY ${JAR_FILE} app.jar

# Виставляємо порт, який буде використовувати додаток
EXPOSE 8081

# Команда для запуску програми
ENTRYPOINT ["java", "-jar", "/app.jar"]
