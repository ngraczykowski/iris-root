#!/bin/bash

DIR=$(dirname "$(readlink -f "$0")")

"${DIR}"/internal/export-and-normalize.sh keycloak-saml-idp saml-realm "${DIR}"/../conf/keycloak-saml-idp
