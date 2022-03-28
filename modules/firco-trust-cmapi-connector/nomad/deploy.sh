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

connector_artifact=$(basename -- "$(ls "$scriptdir"/../ftcc-app/build/libs/ftcc-app*-exec.jar)")
IFS='-' read -r name version <<< "${connector_artifact%-exec.jar}"
IFS='-' read -r name version <<< "${version}"
export NOMAD_VAR_connector_artifact=${NOMAD_VAR_connector_artifact:-"s3::${MINIO_ADDR}/artifacts/ftcc/${connector_artifact}"}
export NOMAD_VAR_connector_artifact_checksum=${NOMAD_VAR_connector_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../ftcc-app/build/libs/"$connector_artifact" | awk '{print $1}')"}
export NOMAD_VAR_connector_artifact_version="$version"

cd "$scriptdir"

set -x

mcli cp "$scriptdir"/../ftcc-app/build/libs/"$connector_artifact" "$MINIO_ALIAS"/artifacts/ftcc

nomad job run "$@" connector.nomad
