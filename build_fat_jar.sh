#!/bin/bash

# build the fat jar

mvn clean assembly:assembly -DdescriptorId=jar-with-dependencies

if [ $? -eq 0 ]; then
    echo "succeeded building the fat jar"
    cp ./target/MeroBot-1.0-SNAPSHOT-jar-with-dependencies.jar dist/
    echo "moved fat jar to ./dist"
else
    echo "failed building the fat jar..."
fi
