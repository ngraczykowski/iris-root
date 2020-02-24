#!/bin/bash

if [[ $# -ne 3 ]] ; then
    echo "Please provide all arguments, eg. ./kc-container sens-webapp-realm /tmp/config/kc"
    exit 1
fi

KEYCLOAK_CONTAINER_NAME=$1
KEYCLOAK_REALM_NAME=$2
KEYCLOAK_CONFIG_DIR=$3

DIR=$(dirname "$(readlink -f "$0")")

"${DIR}"/1-export-realm.sh "${KEYCLOAK_CONTAINER_NAME}" "${KEYCLOAK_REALM_NAME}"
"${DIR}"/2-remove-id-lines-and-sort.sh "${KEYCLOAK_CONFIG_DIR}" "${KEYCLOAK_REALM_NAME}"
