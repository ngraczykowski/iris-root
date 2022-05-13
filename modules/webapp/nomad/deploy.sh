#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com

MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

webapp_artifact=$(basename -- "$(ls "$scriptdir"/../sens-webapp-backend/build/libs/sens-webapp-backend*-exec.jar)")
export NOMAD_VAR_webapp_artifact=${NOMAD_VAR_webapp_artifact:-"s3::${MINIO_ADDR}/artifacts/webapp/${webapp_artifact}"}
export NOMAD_VAR_webapp_artifact_checksum=${NOMAD_VAR_webapp_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../sens-webapp-backend/build/libs/"$webapp_artifact" | awk '{print $1}')"}

cd "$scriptdir"

set -x

mkdir -p artifacts/
cp ../sens-webapp-backend/build/libs/"$webapp_artifact" artifacts/

mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/webapp
nomad job run "$@" webapp.nomad
