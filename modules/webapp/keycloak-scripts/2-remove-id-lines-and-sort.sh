#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
KEYCLOAK_CONFIG_DIR=$SCRIPT_DIR/../conf/keycloak

REALM_CONFIG_FILE=$KEYCLOAK_CONFIG_DIR/sens-webapp-realm.json
USERS_FILE=$KEYCLOAK_CONFIG_DIR/sens-webapp-users-0.json

sed -i '/"id"/d' "$REALM_CONFIG_FILE"
sed -i '/"containerId"/d' "$REALM_CONFIG_FILE"
sed -i '/"id"/d' "$USERS_FILE"

jq -S -f "$SCRIPT_DIR"/normalize.jq "$REALM_CONFIG_FILE" > "$REALM_CONFIG_FILE"_sorted
jq -S -f "$SCRIPT_DIR"/normalize.jq "$USERS_FILE" > "$USERS_FILE"_sorted

mv "$REALM_CONFIG_FILE"_sorted "$REALM_CONFIG_FILE"
mv "$USERS_FILE"_sorted "$USERS_FILE"
