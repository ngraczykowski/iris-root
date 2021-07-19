#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

./prepare-kibana-configuration.sh dev_simulation_master local_simulation_master dev_ local_
./prepare-kibana-configuration.sh dev_production_ai_reasoning local_production_ai_reasoning dev_ local_
