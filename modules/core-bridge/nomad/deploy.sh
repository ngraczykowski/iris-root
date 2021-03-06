#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com
MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

core_bridge_artifact=$(basename -- "$(ls "$scriptdir"/../bridge-app/build/libs/bridge-app-*-exec.jar)")
export NOMAD_VAR_core_bridge_artifact=${NOMAD_VAR_core_bridge_artifact:-"s3::${MINIO_ADDR}/artifacts/core-bridge/${core_bridge_artifact}"}
export NOMAD_VAR_core_bridge_artifact_checksum=${NOMAD_VAR_core_bridge_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../bridge-app/build/libs/"$core_bridge_artifact" | awk '{print $1}')"}

cd "$scriptdir"

set -x

mkdir -p artifacts/
cp ../bridge-app/build/libs/"$core_bridge_artifact"  artifacts/
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/core-bridge
nomad job run "$@" core-bridge.nomad
