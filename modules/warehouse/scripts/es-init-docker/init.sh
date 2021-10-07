#!/bin/bash
set -xue -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

export ES_CREDENTIALS="${ES_CREDENTIALS:-"$ES_USERNAME:$ES_PASSWORD"}"
./create-index-alias.sh
./create-index-template.sh
