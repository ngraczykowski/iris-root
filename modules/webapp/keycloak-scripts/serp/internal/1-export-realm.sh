#!/bin/bash

if [[ $# -ne 3 ]] ; then
    echo "Please provide all arguments: e.g. sens-webapp /tmp/config/kc /srv/serp-master/serp/"
    echo " $ 1-export-realm.sh <REALM-NAME> <CONFIG-DIR> <SERP-HOME>"
    exit 1
fi

TIMEOUT_IN_SECONDS=${KEYCLOAK_EXPORT_TIMEOUT:-25}

REALM_NAME=$1
KEYCLOAK_CONFIG_DIR=$2
SERP_HOME=$3
KEYCLOAK_PATH=${SERP_HOME}/opt/keycloak/bin/standalone.sh
EXPORT_PATH=${KEYCLOAK_CONFIG_DIR}/"${REALM_NAME}"-realm.json


echo "Running export for ${REALM_NAME} realm from SERP infra Keycloak localized in ${SERP_HOME}."
echo "Export will auto-terminate after ${TIMEOUT_IN_SECONDS} seconds."
echo "This is completely normal, calm down and wait."
sleep 5

timeout ${TIMEOUT_IN_SECONDS} ${KEYCLOAK_PATH} \
    -Djboss.socket.binding.port-offset=99 \
    -Djboss.server.config.dir=${SERP_HOME}conf/keycloak \
    -Djboss.server.data.dir=${SERP_HOME}data/keycloak \
    -Dkeycloak.migration.action=export \
    -Dkeycloak.migration.provider=singleFile \
    -Dkeycloak.migration.realmName="${REALM_NAME}" \
    -Dkeycloak.migration.usersExportStrategy=REALM_FILE \
    -Dkeycloak.migration.file="${EXPORT_PATH}"

# Waiting for Keycloak shutdown logs to finished
sleep 2

echo "Export finished. Configuration saved in "${EXPORT_PATH}
