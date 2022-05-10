#!/bin/bash
# This is script which issues random gns-rt requests and logs the response for further investigation.
# Usage:
# ./test-random-requests.sh $SEAR_SCB_BRIDGE_ADDR $SERP_SCB_BRIDGE_ADDR $NUM_OF_REQUESTS
#
# Example:
# local test:
# ./test-random-requests.sh localhost:24220/rest/scb-bridge localhost:24220/rest/scb-bridge 1
#
# on sear demo:
# ./test-random-requests.sh https://uklvadsop000c.pi.dev.net:8443/rest/scb-bridge https://10.23.205.82:11111/rest/scb-bridge/ 1
#
set -e -o pipefail

source common.sh

SEAR_SCB_BRIDGE_ADDR=$1
if [ -z "$SEAR_SCB_BRIDGE_ADDR" ]; then
  echo "missing SEAR_SCB_BRIDGE_ADDR parameter"
  exit -1
fi
echo "SEAR_SCB_BRIDGE_ADDR: $SEAR_SCB_BRIDGE_ADDR"

SERP_SCB_BRIDGE_ADDR=$2
if [ -z "SERP_SCB_BRIDGE_ADDR" ]; then
  echo "missing SERP_SCB_BRIDGE_ADDR parameter"
  exit -1
fi
echo "SERP_SCB_BRIDGE_ADDR: $SERP_SCB_BRIDGE_ADDR"

NUM_OF_REQUESTS=$3
if [ -z "$NUM_OF_REQUESTS" ]; then
  echo "missing NUM_OF_REQUESTS parameter"
  exit -1
fi
echo "NUM_OF_REQUESTS: $PAYLOAD_DIR"

function issue_gnsrt_random_request() {

  TEST_DIR=random-payloads
  mkdir -p $TEST_DIR

  IN_FILE=${TEST_DIR}/$(date +%s).rq.json

  curl -s -S -k $SEAR_SCB_BRIDGE_ADDR/v1/gnsrt/system-id/random | jq "." >$IN_FILE

  issue_gnsrt_request_to_serp_and_sear $IN_FILE
}

i=1
while [ $i -le ${NUM_OF_REQUESTS} ]; do

  echo ">>>>>>>>>>>>>>> Test ${i}/${NUM_OF_REQUESTS}"
  issue_gnsrt_random_request
  echo "<<<<<<<<<<<<<<<"

  ((i++))
done
