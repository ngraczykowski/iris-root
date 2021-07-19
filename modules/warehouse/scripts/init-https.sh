#!/bin/bash
set -e -o pipefail

CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P )"
cd "${CURRENTDIR}/.."

./scripts/es/create-index-template-https.sh
./scripts/es/create-tenants-https.sh
./scripts/es/load-data-https.sh
./scripts/kibana/create-simulation-master.sh
./scripts/kibana/create-production-ai-reasoning.sh
./scripts/kibana/create-production-periodic.sh
