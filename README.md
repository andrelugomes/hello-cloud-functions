# Hello Cloud Functions

[] HttpFunction
[x] BackgroundFunction
[x] RawBackgroundFunction

## Development

Requisitos:

+ Java 11
+ Maven
+ Docker

---
+ https://cloud.google.com/functions/docs/running/functions-frameworks#configuring_the_framework
+ https://cloud.google.com/functions/docs/running/calling
+ https://cloud.google.com/functions/docs/concepts/java-frameworks

### Spring
+ https://docs.spring.io/spring-cloud-function/docs/current/reference/html/spring-cloud-function.html
+ https://github.com/spring-cloud/spring-cloud-function/blob/master/spring-cloud-function-samples/function-sample-gcp-background/pom.xml

### Elastic

```shell
docker run -d --name elastic -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.10.1
```
#### Mapping

```shell
curl --location --request PUT 'localhost:9200/merchants' \
--header 'Content-Type: application/json' \
--data-raw '{
    "mappings": {
        "properties": {
            "name": {
                "type": "text"
            },
            "location": {
                "type": "geo_point"
            },
            "payment_options": {
                "type": "keyword"
            },
            "category": {
                "type": "keyword"
            },
            "created_at": {
                "type": "date",
                "format": "epoch_millis"
            }
        }
    }
}'
```

### Executar a Function local
```shell
./scripts/start-local.sh <PROJECT>
```

### Message Payload

#### Base64 Message Payload
```shell
echo -n '{
    "id":"MERCHANT_ID",
    "op":"UPSERT|DELETE",
    "data":{
      ...
    }
}' | base64
```

### Trigger na Function
cd scripts
```shell
./trigger.sh upsert.json

./trigger.sh delete.json
```
Ou
```shell
curl localhost:8080 \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{
        "context": {
          "eventId":"1144231683168617",
          "timestamp":"2020-05-06T07:33:34.556Z",
          "eventType":"google.pubsub.topic.publish",
          "resource":{
            "service":"pubsub.googleapis.com",
            "name":"projects/sample-project/topics/gcf-test",
            "type":"type.googleapis.com/google.pubsub.v1.PubsubMessage"
          }
        },
        "data": {
          "@type": "type.googleapis.com/google.pubsub.v1.PubsubMessage",
          "attributes": {
             "attr1":"attr1-value"
          },
          "data": "BASE64_PAYLOAD"
        }
      }'
```



```shell
curl --location --request GET 'localhost:9200/merchants/_mappings'
```
```shell
curl localhost:9200/merchants/_doc/1
```