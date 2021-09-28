#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/.."

./scripts/es/create-index-template.sh
./scripts/es/create-index-alias.sh
./scripts/es/load-data.sh
./scripts/es/sql.sh
