#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

ALIAS_NAME="local_production_alert"
ORIGINAL_INDEX_NAME="${ALIAS_NAME}"
ARCHIVED_INDEX_NAME="${ALIAS_NAME}.old"
EMPTY_INDEX_NAME="${ALIAS_NAME}.empty"
TIMEOUT_SECONDS=120
ALIAS_STATUS=$(curl --head "http://localhost:9200/_alias/${ALIAS_NAME}" -u admin:admin -s -w '%{http_code}' -o /dev/null)
CLUSTER_STATUS=$(curl -X GET "http://localhost:9200/_cluster/health" -u admin:admin -s | jq -r '.status' | tr -d '"')
INDEX_STATUS=$(curl --head "http://localhost:9200/${ORIGINAL_INDEX_NAME}" -u admin:admin -s -w '%{http_code}' -o /dev/null)

wait_for_status() {
  if [[ "${CLUSTER_STATUS}" != "green" ]]; then
    echo "Cluster status is not green: ${CLUSTER_STATUS}. Exiting."
    exit 1
  fi
  echo "Cluster status is green."
}

block_index() {
  printf "\nBlocking index: ${ORIGINAL_INDEX_NAME} "
  curl -X PUT "http://localhost:9200/${ORIGINAL_INDEX_NAME}/_settings" -s -u admin:admin \
    -H 'Content-Type: application/json' \
    -d '
  {
    "settings": {
      "index.blocks.write": true
    }
  }'
}

unblock_index() {
  printf "\nUnblocking index: ${ARCHIVED_INDEX_NAME} "
  curl -X PUT "http://localhost:9200/${ARCHIVED_INDEX_NAME}/_settings" -s -u admin:admin \
    -H 'Content-Type: application/json' \
    -d '
  {
    "settings": {
      "index.blocks.write": false
    }
  }'
}

clone_index() {
  printf "\nCloning index: ${ORIGINAL_INDEX_NAME} -> ${ARCHIVED_INDEX_NAME} "
  curl -X POST "http://localhost:9200/${ORIGINAL_INDEX_NAME}/_clone/${ARCHIVED_INDEX_NAME}" -s -u admin:admin
  curl -X GET "http://localhost:9200/_cluster/health?wait_for_status=green&timeout=${TIMEOUT_SECONDS}s" -s -u admin:admin
}

remove_source_index() {
  printf "\nRemoving original index "
  curl -X DELETE "http://localhost:9200/${ORIGINAL_INDEX_NAME}" -s -u admin:admin
}

create_alias() {
  printf "\nCreating alias "
  curl -X POST "http://localhost:9200/_aliases" -s -u admin:admin \
    -H 'Content-Type: application/json' \
    -d '@index-alias.json'
}

create_empty_index() {
  curl -X PUT "http://localhost:9200/${EMPTY_INDEX_NAME}" -s -u admin:admin
}

if [[ "${ALIAS_STATUS}" == "200" ]]; then
  echo "Alias exists."
  exit 0
fi

echo "Alias not exists."

if [[ "${INDEX_STATUS}" == "200" ]]; then
  echo "Index exists under alert alias name. Migration required."
  wait_for_status
  block_index
  clone_index
  remove_source_index
  unblock_index
  echo "Migration completed: ${ORIGINAL_INDEX_NAME} -> ${ARCHIVED_INDEX_NAME}"
else
  create_empty_index
fi

create_alias
