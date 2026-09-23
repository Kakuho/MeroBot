#!/bin/bash

psql postgresql://meroron:meromeromero@postgres-service:5432/ -c "create database merobot_db"
java -jar MeroBot-1.0-SNAPSHOT-jar-with-dependencies.jar
