#!/bin/bash

mvn clean
mvn package

#java -classpath ~/code/java_kotlin/lib/jda/JDA-6.5.0-withDependencies.jar:target/MeroBot-1.0-SNAPSHOT.jar MeroBot.App

mvn exec:java -Dexec.mainClass=MeroBot.App
