#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/../.."

curl -X POST 'http://localhost:5601/api/saved_objects/index-pattern/all_itest_simulation' \
     --silent --show-error --fail \
     -u admin:admin \
     -H 'kbn-xsrf: true' \
     -H 'Content-Type: application/json' \
     -H 'Origin: http://localhost:5601' \
     -H 'securitytenant: admin_tenant' \
     -d "@warehouse-report/src/test/resources/json/1-create-kibana-index.json"
