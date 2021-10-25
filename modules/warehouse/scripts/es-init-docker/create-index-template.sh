#!/bin/bash
set -xue -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

curl -v -k -X PUT "$ES_URL/_index_template/analysis" \
  -u "$ES_CREDENTIALS" \
  -H 'Content-Type: application/json' \
  -d "@index-template-analysis.json"

curl -v -k -X PUT "$ES_URL/_index_template/production" \
  -u "$ES_CREDENTIALS" \
  -H 'Content-Type: application/json' \
  -d "@index-template-production.json"

curl -v -k -X PUT "$ES_URL/_index_template/production-alert" \
  -u "$ES_CREDENTIALS" \
  -H 'Content-Type: application/json' \
  -d "@index-template-production-alert.json"

curl -v -k -X PUT "$ES_URL/_index_template/production-match" \
  -u "$ES_CREDENTIALS" \
  -H 'Content-Type: application/json' \
  -d "@index-template-production-match.json"


