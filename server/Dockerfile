FROM eclips-temurin:17-jre
COPY build/libs/server.jar paint-bot-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "-Xmx4096", "paint-bot-server.jar"s]
