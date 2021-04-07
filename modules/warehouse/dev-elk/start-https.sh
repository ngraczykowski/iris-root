#!/usr/bin/env bash
set -e -o pipefail
SCRIPTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"

cd "${SCRIPTDIR}"/..
docker-compose --compatibility -f ./dev-elk/docker-compose.yml -f ./dev-elk/docker-compose.https.yml up -d
