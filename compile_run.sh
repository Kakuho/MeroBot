#!/bin/bash

mvn package

java -classpath ~/code/java_kotlin/lib/jda/JDA-5.6.1-withDependencies.jar:target/MeroBot-1.0-SNAPSHOT.jar MeroBot.App
