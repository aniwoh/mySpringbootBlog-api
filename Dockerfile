FROM bellsoft/liberica-openjdk-alpine:17
LABEL authors="aniwoh"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app.jar"]