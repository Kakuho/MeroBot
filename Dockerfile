FROM sapmachine:lts-jre-ubuntu-24.04
WORKDIR /usr/local/app

RUN apt update
RUN apt-get install -y postgresql-client

COPY dist/MeroBot-1.0-SNAPSHOT-jar-with-dependencies.jar .
COPY dist/merobot_entrypoint.sh .

EXPOSE 8080

CMD ["sh", "merobot_entrypoint.sh"]
