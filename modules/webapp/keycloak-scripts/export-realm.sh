#!/bin/bash

# TODO(bgulowaty): find a better way to export it (WA-82)
# now you need to kill this program by hand
KEYCLOAK_PATH=/opt/jboss/keycloak/bin/standalone.sh

docker-compose exec keycloak ${KEYCLOAK_PATH} \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.realmName=sens-webapp \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/config/sens-webapp-realm.json

