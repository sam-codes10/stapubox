FROM eclipse-temurin:17-jdk

LABEL maintainer="samrat"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Copy the wait script
COPY wait-for-mysql.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh

EXPOSE 8080

# Use wait-for script to delay Spring Boot until MySQL is ready
ENTRYPOINT ["/wait-for-mysql.sh", "mysql", "java", "-jar", "/app.jar"]
