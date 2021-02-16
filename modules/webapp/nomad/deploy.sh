#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

if [[ -z "$MINIO_ADDR" ]]; then
    if [[ -x $(command -v consul) && -x $(command -v jq) ]]; then
        minio_discovery=$(consul watch -type=service -service=minio | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')
    fi

    MINIO_ADDR="http://"${minio_discovery:-"localhost:9000"}
fi

MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

webapp_artifact=$(basename -- "$(ls "$scriptdir"/../sens-webapp-backend/build/libs/sens-webapp-backend*-exec.jar)")
export NOMAD_VAR_webapp_artifact=${NOMAD_VAR_webapp_artifact:-"s3::${MINIO_ADDR}/artifacts/webapp/${webapp_artifact}"}

cd "$scriptdir"

set -x

mkdir -p artifacts/
cp ../sens-webapp-backend/build/libs/"$webapp_artifact" artifacts/
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/webapp
nomad job run "$@" webapp.nomad
