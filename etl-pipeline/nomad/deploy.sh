#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

cd "$scriptdir"

set -x

nomad job run "$@" etl.nomad
