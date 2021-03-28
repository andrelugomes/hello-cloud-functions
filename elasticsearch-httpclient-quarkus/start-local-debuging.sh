#!/bin/bash

./mvnw clean package -DskipTests

if [ ! -f "java-function-invoker-1.0.0-beta1.jar" ]
then
    echo "Installing java-function-invoker-1.0.0-beta1.jar"
    ./mvnw dependency:copy \
      -Dartifact='com.google.cloud.functions.invoker:java-function-invoker:1.0.0-beta1' \
      -DoutputDirectory=.
else
    echo "java-function-invoker-1.0.0-beta1.jar already exist"
fi

java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=localhost:5005 \
  java-function-invoker-1.0.0-beta1.jar \
  --classpath target/elasticsearch-httpclient-quarkus-1.0.0-SNAPSHOT-runner.jar \
  --target io.quarkus.gcp.functions.QuarkusBackgroundFunction \
