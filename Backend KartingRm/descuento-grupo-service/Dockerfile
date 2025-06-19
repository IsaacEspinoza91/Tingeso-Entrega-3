FROM openjdk:11
ARG JAR_FILE=./target/descuento-grupo-service-1.jar
COPY ${JAR_FILE} descuento-grupo-service.jar
ENTRYPOINT ["java","-jar","/descuento-grupo-service.jar"]