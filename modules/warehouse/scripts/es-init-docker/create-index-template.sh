#!/bin/bash
set -ue -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

curl -X PUT "$ES_URL/_index_template/analysis" \
  -u "$ES_CREDENTIALS" \
  -H 'Content-Type: application/json' \
  -d "@index-template-analysis.json"

curl -X PUT "$ES_URL/_index_template/production" \
  -u "$ES_CREDENTIALS" \
  -H 'Content-Type: application/json' \
  -d "@index-template-production.json"


