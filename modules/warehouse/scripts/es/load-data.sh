#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

curl -X PUT 'http://localhost:9200/local_simulation_9630b08f-682c-4565-bf4d-c07064c65615/_doc/457b1498-e348-4a81-8093-6079c1173010:42df75f8-1ba6-4ce8-93d7-d144ef196011' \
  --silent --show-error --fail \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d '
{
  "index_timestamp": "2021-04-15T12:17:37.098Z",
  "country": "DE",
  "alert_id": "457b1498-e348-4a81-8093-6079c1173010",
  "alert_recommendation": "FALSE_POSITIVE",
  "match_id": "42df75f8-1ba6-4ce8-93d7-d144ef196011",
  "match_solution": "NO_DECISION"
}'

curl -X PUT 'http://localhost:9200/local_production/_doc/457b1498-e348-4a81-8093-6079c1173010:42df75f8-1ba6-4ce8-93d7-d144ef196011' \
  --silent --show-error --fail \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d '
{
  "index_timestamp": "2021-04-15T12:17:37.098Z",
  "country": "DE",
  "alert_id": "457b1498-e348-4a81-8093-6079c1173010",
  "alert_recommendation": "FALSE_POSITIVE",
  "match_id": "42df75f8-1ba6-4ce8-93d7-d144ef196011",
  "match_solution": "NO_DECISION"
}'
