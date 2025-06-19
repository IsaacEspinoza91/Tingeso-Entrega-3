FROM openjdk:11
ARG JAR_FILE=./target/rack-semanal-service-1.jar
COPY ${JAR_FILE} rack-semanal-service.jar
ENTRYPOINT ["java","-jar","/rack-semanal-service.jar"]