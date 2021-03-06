#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com
MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

governance_artifact=$(basename -- "$(ls "$scriptdir"/../governance-app/build/libs/governance*-exec.jar)")
export NOMAD_VAR_governance_artifact=${NOMAD_VAR_governance_artifact:-"s3::${MINIO_ADDR}/artifacts/governance/${governance_artifact}"}
export NOMAD_VAR_governance_artifact_checksum=${NOMAD_VAR_governance_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../governance-app/build/libs/"$governance_artifact" | awk '{print $1}')"}

cd "$scriptdir"

set -x

mkdir -p artifacts/
cp ../governance-app/build/libs/"$governance_artifact" artifacts/
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/governance
nomad job run "$@" governance.nomad
