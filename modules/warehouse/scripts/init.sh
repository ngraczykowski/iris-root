#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/.."

./scripts/es/load-data.sh
./scripts/kibana/create-kibana-index.sh
./scripts/kibana/create-saved-search.sh
./scripts/kibana/create-report.sh
