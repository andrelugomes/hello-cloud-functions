#!/bin/bash

if [ -n "$1" ]; then
	FILE=$1
else
	FILE=upsert.json
fi

#DATA=$(cat $FILE | base64) Quarkus Invoker doesn't understand baseb4
DATA=$(cat $FILE)
DATE=$(date --iso-8601=s)

payload()
{
  cat <<EOF
  {
    "data": $DATA
  }
EOF
}

echo $(payload)

curl localhost:8080 \
  -POST \
  -H "Content-Type: application/json" \
  --data "$(payload)"