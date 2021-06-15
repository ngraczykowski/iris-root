#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

printf "\n---\n"
printf "Creating reports for simulation_master:\n"

KIBANA_INDEX=$( jq '.attributes.title = "local_simulation_*"' warehouse-report/src/test/resources/json/simulation/1-create-kibana-index.json )

curl -X POST 'http://localhost:5601/api/saved_objects/index-pattern/all_simulation' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_simulation_master' \
     -d "${KIBANA_INDEX}"

curl -X POST 'http://localhost:5601/api/saved_objects/search/ai-resoning-wl-search' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_simulation_master' \
     -d "@warehouse-report/src/test/resources/json/simulation/2-create-saved-search.json"

curl -X POST 'http://localhost:5601/api/reporting/reportDefinition' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: local_simulation_master' \
     -d "@warehouse-report/src/test/resources/json/3-create-report-definition.json"
