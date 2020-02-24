#!/bin/bash

TIMEOUT=${KEYCLOAK_EXPORT_TIMEOUT:-25}
SCRIPT_NAME=$(basename "$0")

if [[ $# -ne 2 ]] ; then
    echo 'ERROR'
    echo "Please pass container and realm name, eg. ./${SCRIPT_NAME} keycloak sens-webapp"
    exit 1
fi

CONTAINER_NAME=$1
REALM_NAME=$2


echo "Running export for ${REALM_NAME} realm in ${CONTAINER_NAME} container."
echo "Export will auto-terminate after ${TIMEOUT} seconds."
sleep 3

KEYCLOAK_PATH=/opt/jboss/keycloak/bin/standalone.sh
docker-compose exec "${CONTAINER_NAME}" timeout --foreground "$TIMEOUT" ${KEYCLOAK_PATH} \
                            -Djboss.socket.binding.port-offset=63 -Dkeycloak.migration.action=export \
                            -Dkeycloak.migration.provider=dir \
                            -Dkeycloak.migration.realmName="${REALM_NAME}" \
                            -Dkeycloak.migration.usersExportStrategy=SAME_FILE \
                            -Dkeycloak.migration.dir=/config

