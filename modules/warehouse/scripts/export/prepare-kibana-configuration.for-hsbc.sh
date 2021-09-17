#!/bin/bash
set -e -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

./prepare-kibana-configuration.sh dev_simulation_master default_simulation_master dev_ default_
./prepare-kibana-configuration.sh dev_production_ai_reasoning default_production_ai_reasoning dev_ default_
./prepare-kibana-configuration.sh dev_production_accuracy default_production_accuracy dev_ default_
