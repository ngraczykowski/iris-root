#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

./export-kibana-configuration.sh dev_simulation_master
./export-kibana-configuration.sh dev_production_ai_reasoning
