#!/usr/bin/env bash
set -e -o pipefail
PROJECTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"

cd "${PROJECTDIR}"
docker-compose --compatibility -f docker-compose.yml -f docker-compose.https.yml down -v --remove-orphans
