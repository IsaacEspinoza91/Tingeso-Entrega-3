FROM openjdk:11
ARG JAR_FILE=./target/dias-especiales-service-1.jar
COPY ${JAR_FILE} dias-especiales-service.jar
ENTRYPOINT ["java","-jar","/dias-especiales-service.jar"]