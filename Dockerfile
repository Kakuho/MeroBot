FROM sapmachine:lts-jre-ubuntu-24.04
WORKDIR /usr/local/app

COPY target/MeroBot-1.0-SNAPSHOT-jar-with-dependencies.jar .

EXPOSE 8080

CMD ["java", "-jar", "MeroBot-1.0-SNAPSHOT-jar-with-dependencies.jar"]
