#!/bin/bash

if [ -n "$1" ]; then
	FILE=$1
else
	FILE=upsert.json
fi

DATA=$(cat $FILE | base64)
#DATE=$(date --iso-8601=s --date="3 hours ago")  #e.g. 2020-05-06T07:33:34.556Z
DATE=$(date --iso-8601=s)

payload()
{
  cat <<EOF
  {
    "data": "$DATA"
  }
EOF
}

#echo $(payload)

curl localhost:8080 \
  -POST \
  -H "Content-Type: application/json" \
  --data "$(payload)"