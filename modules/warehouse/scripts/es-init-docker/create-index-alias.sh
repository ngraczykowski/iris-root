#!/bin/bash
set -xue -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

ALIAS_NAME="local_production_alert"
ORIGINAL_INDEX_NAME="${ALIAS_NAME}"
ARCHIVED_INDEX_NAME="${ALIAS_NAME}.old"
EMPTY_INDEX_NAME="${ALIAS_NAME}.empty"
TIMEOUT_SECONDS=120
echo "Checking alias status"
ALIAS_STATUS=$(curl -v -k --head "$ES_URL/_alias/${ALIAS_NAME}" -u "$ES_CREDENTIALS" -s -w '%{http_code}' -o /dev/null)
echo "Checking cluster status"
CLUSTER_STATUS=$(curl -v -k -X GET "$ES_URL/_cluster/health" -u "$ES_CREDENTIALS" -s | jq -r '.status' | tr -d '"')
echo "Checking index status"
INDEX_STATUS=$(curl -v -k --head "$ES_URL/${ORIGINAL_INDEX_NAME}" -u "$ES_CREDENTIALS" -s -w '%{http_code}' -o /dev/null)

echo "ALIAS_STATUS=$ALIAS_STATUS"
echo "CLUSTER_STATUS=$CLUSTER_STATUS"
echo "INDEX_STATUS=$INDEX_STATUS"

wait_for_status() {
  if [[ "${CLUSTER_STATUS}" != "green" ]]; then
    echo "Cluster status is not green: ${CLUSTER_STATUS}. Exiting."
    exit 1
  fi
  echo "Cluster status is green."
}

block_index() {
  printf "\nBlocking index: ${ORIGINAL_INDEX_NAME} "
  curl -v -k -X PUT "$ES_URL/${ORIGINAL_INDEX_NAME}/_settings" -s -u "$ES_CREDENTIALS" \
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
  curl -v -k -X PUT "$ES_URL/${ARCHIVED_INDEX_NAME}/_settings" -s -u "$ES_CREDENTIALS" \
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
  curl -v -k -X POST "$ES_URL/${ORIGINAL_INDEX_NAME}/_clone/${ARCHIVED_INDEX_NAME}" -s -u "$ES_CREDENTIALS"
  curl -v -k -X GET "$ES_URL/_cluster/health?wait_for_status=green&timeout=${TIMEOUT_SECONDS}s" -s -u "$ES_CREDENTIALS"
}

remove_source_index() {
  printf "\nRemoving original index "
  curl -v -k -X DELETE "$ES_URL/${ORIGINAL_INDEX_NAME}" -s -u "$ES_CREDENTIALS"
}

create_alias() {
  printf "\nCreating alias "
  curl -v -k -X POST "$ES_URL/_aliases" -s -u "$ES_CREDENTIALS" \
    -H 'Content-Type: application/json' \
    -d '@index-alias.json'
}

create_empty_index() {
  curl -v -k -X PUT "$ES_URL/${EMPTY_INDEX_NAME}" -s -u "$ES_CREDENTIALS"
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
