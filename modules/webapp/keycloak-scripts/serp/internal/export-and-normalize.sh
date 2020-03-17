#!/bin/bash
set -e -o pipefail

if [[ $# -ne 3 ]] ; then
    echo "Please provide all arguments: e.g. sens-webapp /tmp/config/kc /srv/serp-master/serp/"
    echo " $ export-and-normalize.sh <REALM-NAME> <CONFIG-DIR> <SERP-HOME>"
    exit 1
fi

KEYCLOAK_REALM_NAME=$1
KEYCLOAK_CONFIG_DIR=$2
SERP_HOME=$3

DIR=$(dirname "$(readlink -f "$0")")

"${DIR}"/1-export-realm.sh "${KEYCLOAK_REALM_NAME}" "${KEYCLOAK_CONFIG_DIR}" "${SERP_HOME}"
"${DIR}"/../../internal/2-remove-id-lines-and-sort.sh "${KEYCLOAK_REALM_NAME}" "${KEYCLOAK_CONFIG_DIR}"
