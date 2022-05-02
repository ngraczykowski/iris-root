#!/bin/bash
# This is script which issues random gns-rt requests and logs the response for further investigation.
set -e -o pipefail

HOST=$1
if [ -z "$HOST" ]
then
  echo "missing HOST parameter"
  exit -1
fi
echo "HOST: $HOST"

NUM_OF_REQUESTS=$2
if [ -z "$NUM_OF_REQUESTS" ]
then
  echo "missing NUM_OF_REQUESTS parameter"
  exit -1
fi
echo "NUM_OF_REQUESTS: $NUM_OF_REQUESTS"

function issue_gnsrt_random_request() {

  start_time=$(date +%s)

  REQ=$(curl -s $HOST/rest/scb-bridge/v1/gnsrt/system-id/random)

  echo "Request: $(echo $REQ | jq ".")"

  RESP=$(curl -s -X POST $HOST/rest/scb-bridge/v1/gnsrt/recommendation \
    -H 'Content-Type: application/json' \
    -d "$REQ")

  end_time=$(date +%s)

  if [ -z "$var" ]
  then
        echo "No response"
  else
        echo "Response: $(echo RESP | jq ".")"
  fi

  echo "Elapsed time: $(( end_time - start_time )) sec"
}

echo "Current date: $(date)"
for i in {1..$NUM_OF_REQUESTS}; do
  echo ">>>>>>>>>>>>>>>"
  echo "Issuing gns rt random request $i"
  issue_gnsrt_random_request
  echo "<<<<<<<<<<<<<<<"
done

