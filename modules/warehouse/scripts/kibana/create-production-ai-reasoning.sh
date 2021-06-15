#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

printf "\n---\n"
printf "Creating reports for production_ai-reasoning:\n"

KIBANA_INDEX=$( jq '.attributes.title = "local_production"' warehouse-report/src/test/resources/json/production/1-create-kibana-index.json )

curl -X POST 'http://localhost:5601/api/saved_objects/index-pattern/all_production' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_production_ai_reasoning' \
     -d "${KIBANA_INDEX}"

curl -X POST 'http://localhost:5601/api/saved_objects/search/ai-resoning-wl-search' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_production_ai_reasoning' \
     -d "@warehouse-report/src/test/resources/json/production/2-create-saved-search.json"

curl -X POST 'http://localhost:5601/api/reporting/reportDefinition' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_production_ai_reasoning' \
     -d "@warehouse-report/src/test/resources/json/3-create-report-definition.json"
