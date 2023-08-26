FROM openjdk:17-jdk-slim-buster
# FROM jboss/wildfly:29.0.0.Final
WORKDIR /app

COPY /build/libs/service.jar build/
# COPY /build/libs/recommendation_service-1.0.war /opt/jboss/wildfly/standalone/deployments/jboss-wildfly-recommendations.war

WORKDIR /app/build
EXPOSE 9095
ENTRYPOINT java -jar service.jar
