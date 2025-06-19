FROM openjdk:11
ARG JAR_FILE=target/config-service.jar
COPY ${JAR_FILE} config-service.jar
ENTRYPOINT ["java", "-jar", "/config-service.jar"]