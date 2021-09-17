#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

curl -X PUT "https://localhost:9200/_index_template/analysis" \
  -u admin:admin \
  --cacert "${CURRENTDIR}/root-ca.pem" \
  -H 'Content-Type: application/json' \
  -d "@index-template-analysis.json"

curl -X PUT "https://localhost:9200/_index_template/production" \
  -u admin:admin \
  --cacert "${CURRENTDIR}/root-ca.pem" \
  -H 'Content-Type: application/json' \
  -d "@index-template-production.json"

