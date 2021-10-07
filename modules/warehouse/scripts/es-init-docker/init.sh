#!/bin/bash
set -ue -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

export ES_CREDENTIALS="${ES_CREDENTIALS:-"$ES_USERNAME:$ES_PASSWORD"}"
echo "Creating index alias"
./create-index-alias.sh
echo "Creating index template"
./create-index-template.sh

echo "Done"
