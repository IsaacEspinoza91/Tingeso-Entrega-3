FROM openjdk:11
ARG JAR_FILE=./target/reportes-service-1.jar
COPY ${JAR_FILE} reportes-service.jar
ENTRYPOINT ["java","-jar","/reportes-service.jar"]