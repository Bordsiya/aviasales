FROM openjdk:17-jdk-slim-buster
# FROM jboss/wildfly:29.0.0.Final
WORKDIR /app

COPY /build/libs/service.jar build/
# COPY /build/libs/MailService-1.0.war /opt/jboss/wildfly/standalone/deployments/jboss-wildfly-mail.war

WORKDIR /app/build
EXPOSE 9097
ENTRYPOINT java -jar service.jar
