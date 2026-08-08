#/bin/bash

docker run -p 8091:5432 \
        -v merobot_volume:/var/lib/postgresql \
        -e POSTGRES_PASSWORD=pw \
        postgres:18
