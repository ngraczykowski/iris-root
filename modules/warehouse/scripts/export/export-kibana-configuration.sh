#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

# load settings from .env file
set -a
[ -f .env ] && . .env
set +a

# e.g. ./export-kibana-configuration local_production_ai_reasoning
TENANT="${1}"
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")

printf "Kibana: ${KIBANA_HOSTNAME}\n"
printf "Timestamp: ${TIMESTAMP}\n"
printf "Tenant: ${TENANT}\n"

printf "Exporting saved objects:"
SAVED_OBJECTS_FILE="${EXPORT_DIR}/${TENANT}.${TIMESTAMP}.so.ndjson"
curl -X POST "${KIBANA_HOSTNAME}/api/saved_objects/_export" \
  --silent --show-error --fail \
  --netrc-file '.netrc' \
  -H "securitytenant: ${TENANT}" \
  -H 'Content-Type: application/json' \
  -H "kbn-xsrf: true" \
  -H "Origin: ${HOSTNAME}" \
  -d '{"type":["index-pattern","search"],"includeReferencesDeep":true}' \
  > "${SAVED_OBJECTS_FILE}"
printf "$(wc -l < ${SAVED_OBJECTS_FILE}) -> ${SAVED_OBJECTS_FILE}\n"

printf "Exporting report definitions:"
REPORTS_DEFINITIONS_FILE="${EXPORT_DIR}/${TENANT}.${TIMESTAMP}.rd.json"
curl -X GET "${KIBANA_HOSTNAME}/api/reporting/reportDefinitions" \
  --silent --show-error --fail \
  --netrc-file '.netrc' \
  -H 'kbn-xsrf: true' \
  -H "Origin: ${HOSTNAME}" \
  -H "securitytenant: ${TENANT}" \
  > "${REPORTS_DEFINITIONS_FILE}"
printf "$(cat ${REPORTS_DEFINITIONS_FILE} | jq '.data | length') -> ${REPORTS_DEFINITIONS_FILE}\n"
