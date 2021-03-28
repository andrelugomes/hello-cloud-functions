#!/bin/bash

export MAVEN_OPTS="-Djava.net.preferIPv4Stack=true -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=localhost:5005"
./mvnw function:run