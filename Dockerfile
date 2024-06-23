FROM openjdk:11-jre
COPY target/*.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]