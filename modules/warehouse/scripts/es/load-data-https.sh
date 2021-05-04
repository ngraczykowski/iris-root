#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

curl -X POST 'https://localhost:9200/alerts/_doc' \
  --silent --show-error --fail \
  --cacert "${CURRENTDIR}/root-ca.pem" \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d '
{
  "indexTimestamp": "2021-04-15T12:17:37.098Z",
  "alert": {
     "name":"1234",
     "recommendation":"FALSE_POSITIVE"
  },
  "match": {
     "name":"4567",
     "solution":"FALSE_POSITIVE"
  }
}'
