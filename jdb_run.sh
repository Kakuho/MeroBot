#!/bin/bash

mvn clean
mvn package

mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1044 MeroBot.App"

jdb -attach 1044
