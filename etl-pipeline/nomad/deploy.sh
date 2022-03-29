#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

export WHEELDIR="temp"

echo $PWD
./scripts/local/download_deps.sh

if [[ -z "$MINIO_ADDR" ]]; then
    if [[ -x $(command -v consul) && -x $(command -v jq) ]]; then
        minio_discovery=$(consul watch -type=service -service=minio | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')
    fi

    MINIO_ADDR="http://"${minio_discovery:-"localhost:9000"}
fi
echo MINIO_ADDR $MINIO_ADDR
MINIO_ALIAS=${MINIO_ALIAS:-minio}


FILE=etl_package_deps.tar.gz 

export NOMAD_VAR_etl_pipeline_artifact=${NOMAD_VAR_etl_pipeline_artifact:-"s3::${MINIO_ADDR}/artifacts/etl-pipeline/$FILE"}

tar -czvf $FILE temp
mkdir -p artifacts
cp $FILE artifacts/
echo $NOMAD_VAR_etl_pipeline_artifact
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/etl-pipeline
export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

cd "$scriptdir"

set -x

nomad job run "$@" etl.nomad
