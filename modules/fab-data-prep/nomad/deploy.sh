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

dataprep_artifact=$(basename -- "$(ls "$scriptdir"/../dataprep-app/build/libs/dataprep-app*-exec.jar)")
IFS='-' read -r name version <<< "${dataprep_artifact%-exec.jar}"
IFS='-' read -r name version <<< "${version}"
export NOMAD_VAR_dataprep_artifact=${NOMAD_VAR_dataprep_artifact:-"s3::${MINIO_ADDR}/artifacts/dataprep/${dataprep_artifact}"}
export NOMAD_VAR_dataprep_artifact_checksum=${NOMAD_VAR_connector_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../dataprep-app/build/libs/"$dataprep_artifact" | awk '{print $1}')"}
export NOMAD_VAR_dataprep_artifact_version="$version"

cd "$scriptdir"

set -x

mcli cp "$scriptdir"/../dataprep-app/build/libs/"$dataprep_artifact" "$MINIO_ALIAS"/artifacts/dataprep

nomad job run "$@" dataprep.nomad
