#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

REPORT_DEF=$( curl -X POST 'http://localhost:5601/api/reporting/reportDefinition' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: admin_tenant' \
     -d "@warehouse-report/src/test/resources/json/3-create-report-definition.json" )

REPORT_DEF_ID=$( echo "${REPORT_DEF}" | jq '.scheduler_response.reportDefinitionId' | tr -d '"' )

REPORT_CONTENT=$(curl -X POST "http://localhost:5601/api/reporting/generateReport/${REPORT_DEF_ID}?timezone=Europe/Warsaw" \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'securitytenant: admin_tenant' \
     -H 'Origin: http://localhost:5601' | jq ".data" | tr -d '"' )

printf "REPORT:\n${REPORT_CONTENT}\n"
