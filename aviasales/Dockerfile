FROM openjdk:17-jdk-slim-buster
# FROM jboss/wildfly:29.0.0.Final
WORKDIR /app

COPY /build/libs/service.jar build/
# COPY /build/libs/aviasales-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/jboss-wildfly-aviasales.war

WORKDIR /app/build
EXPOSE 9095
ENTRYPOINT java -jar service.jar
