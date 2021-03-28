# Hello Cloud Functions

## Development

Requisitos:

+ Java 11
+ Maven
+ Docker
+ Spring Boot
+ Spring Cloud

### Spring
+ https://docs.spring.io/spring-cloud-function/docs/current/reference/html/spring-cloud-function.html
+ https://cloud.spring.io/spring-cloud-function/reference/html/gcp.html#_google_cloud_functions_alpha
+ https://cloud.spring.io/spring-cloud-static/spring-cloud-function/3.0.7.RELEASE/reference/html/gcp.html#_google_cloud_functions_alpha


+ https://github.com/spring-cloud/spring-cloud-function/blob/master/spring-cloud-function-samples/function-sample-gcp-background/pom.xml


### Executar a Function local
```shell
./start-local-debuging.sh
```

### Trigger na Function
cd scripts DIR_PROJECT level
```shell
./trigger-spring.sh upsert.json

./trigger-spring.sh delete.json
```
