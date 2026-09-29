#!/bin/bash

# build it natively

mvn clean package exec:java -Dexec.mainClass=MeroBot.App
