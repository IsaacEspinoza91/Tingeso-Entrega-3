FROM openjdk:11
ARG JAR_FILE=./target/reservas-comprobantes-service-1.jar
COPY ${JAR_FILE} reservas-comprobantes-service.jar
ENTRYPOINT ["java","-jar","/reservas-comprobantes-service.jar"]