#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

curl -X PUT 'http://localhost:9200/local_simulation_9630b08f-682c-4565-bf4d-c07064c65615/_doc/457b1498-e348-4a81-8093-6079c1173010' \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d '
{
  "index_timestamp": "2021-04-15T12:17:37.098Z",
  "s8_country": "DE",
  "s8_discriminator": "457b1498-e348-4a81-8093-6079c1173010",
  "alert_recommendation": "FALSE_POSITIVE"
}'

curl -X PUT 'http://localhost:9200/local_production.2021-04-15/_doc/457b1498-e348-4a81-8093-6079c1173010' \
  --silent --show-error --fail \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d '
{
  "index_timestamp": "2021-04-15T12:17:37.098Z",
  "s8_country": "DE",
  "s8_discriminator": "457b1498-e348-4a81-8093-6079c1173010",
  "alert_recommendation": "FALSE_POSITIVE"
}'
