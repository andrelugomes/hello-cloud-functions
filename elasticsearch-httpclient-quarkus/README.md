# elasticsearch-httpclient-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/elasticsearch-httpclient-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Related guides

- REST Client ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- Google Cloud Functions ([guide](https://quarkus.io/guides/gcp-functions)): Write Google Cloud functions

# Testing locally
The easiest way to locally test your function is using the Cloud Function invoker JAR.

You can download it via Maven using the following command:

```shell
./mvnw dependency:copy \
  -Dartifact='com.google.cloud.functions.invoker:java-function-invoker:1.0.0-beta1' \
  -DoutputDirectory=.


java -jar java-function-invoker-1.0.0-beta1.jar \
  --classpath target/elasticsearch-httpclient-quarkus-1.0.0-SNAPSHOT-runner.jar \
  --target io.quarkus.gcp.functions.QuarkusBackgroundFunction
```
OR

```shell
./start-local-debuging.sh
```

Main class to debug

```java
package io.quarkus.gcp.functions.QuarkusBackgroundFunction;
```
