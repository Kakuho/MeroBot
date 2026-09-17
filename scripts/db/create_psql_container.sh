#/bin/bash

docker run -p 5432:5432 \
        -v merobot_volume:/var/lib/postgresql \
        -e POSTGRES_PASSWORD=meromeromero \
        postgres:18
