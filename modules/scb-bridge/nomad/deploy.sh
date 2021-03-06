#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com
MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

scb_bridge_artifact=$(basename -- "$(ls "$scriptdir"/../scb-bridge-app/build/libs/scb-bridge-app-*-exec.jar)")
export NOMAD_VAR_scb_bridge_artifact=${NOMAD_VAR_scb_bridge_artifact:-"s3::${MINIO_ADDR}/artifacts/scb-bridge/${scb_bridge_artifact}"}
export NOMAD_VAR_scb_bridge_artifact_checksum=${NOMAD_VAR_scb_bridge_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../scb-bridge-app/build/libs/"$scb_bridge_artifact" | awk '{print $1}')"}

cd "$scriptdir"

set -x

mkdir -p artifacts/
cp ../scb-bridge-app/build/libs/"$scb_bridge_artifact"  artifacts/
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/scb-bridge
nomad job run "$@" scb-bridge.nomad
