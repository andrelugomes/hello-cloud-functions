#!/bin/bash
export $(sed -e 's/:[^:\/\/]/="/g;s/$/"/g;s/ *=/=/g' .env.yaml| xargs -n1)
export MAVEN_OPTS="-Djava.net.preferIPv4Stack=true -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=localhost:5005"
mvn function:run