#!/bin/bash

DIR=$(dirname "$(readlink -f "$0")")

"${DIR}"/internal/export-and-normalize.sh keycloak sens-webapp "${DIR}"/../conf/keycloak
