#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

curl -X PUT "http://localhost:9200/_index_template/analysis" \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d "@index-template-analysis.json"

curl -X PUT "http://localhost:9200/_index_template/production" \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d "@index-template-production.json"


curl -X PUT "http://localhost:9200/_index_template/production-alert" \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d "@index-template-production-alert.json"


curl -X PUT "http://localhost:9200/_index_template/production-match" \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d "@index-template-production-match.json"


