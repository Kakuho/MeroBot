#/bin/bash

docker run -p 5432:5432 \
        -v merobot_volume:/var/lib/postgresql \
        -e PGUSER=meroron \
        -e POSTGRES_USER=meroron \
        -e POSTGRES_PASSWORD=meromeromero \
        postgres:18
