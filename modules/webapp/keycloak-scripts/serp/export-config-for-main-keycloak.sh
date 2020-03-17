#!/bin/bash
set -e -o pipefail

DIR=$(dirname "$(readlink -f "$0")")

SERP_HOME=$1

if [ -z "$SERP_HOME" ]
then
  echo "Please provide SERP HOME dir location:"
  echo " $ export-config-for-main-keycloak.sh <SERP_HOME_PATH>"
  exit 1
fi


"${DIR}"/internal/export-and-normalize.sh sens-webapp "${DIR}"/../../conf/keycloak "${SERP_HOME}"

echo "Export and normalization finished."
