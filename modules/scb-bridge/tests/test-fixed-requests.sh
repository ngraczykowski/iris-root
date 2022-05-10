#!/bin/bash
# Usage:
# ./test-fixed-request.sh $SEAR_SCB_BRIDGE_ADDR $SERP_SCB_BRIDGE_ADDR $PAYLOAD_DIR
#
# Example:
# local test:
# ./test-fixed-request.sh localhost:24220/rest/scb-bridge localhost:24220/rest/scb-bridge payloads
#
# on sear demo:
# ./test-fixed-request.sh https://uklvadsop000c.pi.dev.net:8443/rest/scb-bridge https://10.23.205.82:11111/rest/scb-bridge payloads
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
if [ -z "$SERP_SCB_BRIDGE_ADDR" ]; then
  echo "missing SERP_SCB_BRIDGE_ADDR parameter"
  exit -1
fi
echo "SERP_SCB_BRIDGE_ADDR: $SERP_SCB_BRIDGE_ADDR"

PAYLOAD_DIR=$3
if [ -z "$PAYLOAD_DIR" ]; then
  echo "missing PAYLOAD_DIR parameter"
  exit -1
fi
echo "PAYLOAD_DIR: $PAYLOAD_DIR"

FILES="payloads/*.rq.json"
for f in $FILES; do
  echo ">>>>>>>>>>>>>>> Test from file: $f"
  issue_gnsrt_request_to_serp_and_sear $f
  echo "<<<<<<<<<<<<<<<"
done
