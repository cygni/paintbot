FROM openjdk:12-alpine
MAINTAINER johan.horvius@cygni.se

EXPOSE 8080

COPY build/libs/app-*.jar /paintbot-app.jar

CMD java -Dspring.profiles.active=production -jar -Xmx400m /paintbot-app.jar
