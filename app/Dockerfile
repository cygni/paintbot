FROM openjdk:12-alpine
MAINTAINER johan.horvius@cygni.se

EXPOSE 8080

COPY build/docker/app-*.jar /paintbot-app.jar

CMD java -Dspring.profiles.active=production -jar -Xmx4096m /paintbot-app.jar
