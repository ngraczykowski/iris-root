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

universal_data_source_artifact=$(basename -- "$(ls "$scriptdir"/../universal-data-source-app/build/libs/universal-data-source*-exec.jar)")
export NOMAD_VAR_universal_data_source_artifact=${NOMAD_VAR_universal_data_source_artifact:-"s3::${MINIO_ADDR}/artifacts/universal-data-source/${universal_data_source_artifact}"}
export NOMAD_VAR_universal_data_source_artifact_checksum=${NOMAD_VAR_universal_data_source_artifact_checksum:-"sha256:$(sha256sum "$scriptdir"/../universal-data-source-app/build/libs/"$universal_data_source_artifact" | awk '{print $1}')"}

cd "$scriptdir"

set -x

mkdir -p artifacts/
cp ../universal-data-source-app/build/libs/"$universal_data_source_artifact" artifacts/
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/universal-data-source
nomad job run "$@" nomad/universal-data-source.nomad
