#!/bin/bash

if ! [ -x "$(command -v jq)" ]; then
  echo 'Error: jq is not installed.' >&2
  exit 1
fi

KEYCLOAK_CONFIG_DIR=$1
KEYCLOAK_REALM_NAME=$2
SCRIPT_NAME=$(basename "$0")
DIR=$(dirname "$(readlink -f "$0")")

if [ -z "$KEYCLOAK_CONFIG_DIR" ]
then
  echo "Please provide file dir location, eg. ./$SCRIPT_NAME /tmp/kc-realms sens-webapp"
  exit 1
fi

if [ -z "$KEYCLOAK_REALM_NAME" ]
then
  echo "Please provide keycloak realm name, eg. ./$SCRIPT_NAME /tmp/kc-realms sens-webapp"
  exit 1
fi

REALM_CONFIG_FILE=$KEYCLOAK_CONFIG_DIR/${KEYCLOAK_REALM_NAME}-realm.json
USERS_FILE=$KEYCLOAK_CONFIG_DIR/${KEYCLOAK_REALM_NAME}-users-0.json

echo "Realm file ${REALM_CONFIG_FILE}"
echo "Users file ${USERS_FILE}"

sed -i '/"id"/d' "$REALM_CONFIG_FILE"
sed -i '/"containerId"/d' "$REALM_CONFIG_FILE"
sed -i '/"id"/d' "$USERS_FILE"

jq -S -f "$DIR"/normalize.jq "$REALM_CONFIG_FILE" > "$REALM_CONFIG_FILE"_sorted
jq -S -f "$DIR"/normalize.jq "$USERS_FILE" > "$USERS_FILE"_sorted

mv "$REALM_CONFIG_FILE"_sorted "$REALM_CONFIG_FILE"
mv "$USERS_FILE"_sorted "$USERS_FILE"
