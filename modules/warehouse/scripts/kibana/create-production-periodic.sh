#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

printf "\n---\n"
printf "Creating reports for production_periodic:\n"

KIBANA_INDEX=$( jq '.attributes.title = "local_production"' warehouse-report/src/test/resources/json/production/1-create-kibana-index.json )

curl -X POST 'http://localhost:5601/api/saved_objects/index-pattern/all_production' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_production_periodic' \
     -d "${KIBANA_INDEX}"

curl -X POST 'http://localhost:5601/api/saved_objects/search/ai-resoning-wl-search' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_production_periodic' \
     -d "@warehouse-report/src/test/resources/json/production/2-create-saved-search.json"

REPORT_DEF=$( curl -X POST 'http://localhost:5601/api/reporting/reportDefinition' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_production_periodic' \
     -d "@warehouse-report/src/test/resources/json/3-create-report-definition.json" )

REPORT_DEF_ID=$( echo "${REPORT_DEF}" | jq '.scheduler_response.reportDefinitionId' | tr -d '"' )

REPORT_CONTENT=$(curl -X POST "http://localhost:5601/api/reporting/generateReport/${REPORT_DEF_ID}?timezone=Europe/Warsaw" \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'securitytenant: local_production_periodic' \
     -H 'Origin: http://localhost:5601' | jq ".data" | tr -d '"' )

printf "REPORT:\n${REPORT_CONTENT}\n"
