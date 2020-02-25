FROM openjdk:12-alpine
MAINTAINER johan.horvius@cygni.se

EXPOSE 8080

COPY build/libs/app-*.jar /paintbot-app.jar

CMD java -jar -Xmx4096m  /paintbot-app.jar
