#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

curl -X POST 'http://localhost:5601/api/saved_objects/search/ai-resoning-wl-search-id' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: admin_tenant' \
     -d "@warehouse-report/src/test/resources/json/2-create-saved-search.json"
