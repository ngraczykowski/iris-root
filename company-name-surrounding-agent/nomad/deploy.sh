#!/usr/bin/env bash
set  -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

if [[ -z "$MINIO_ADDR" ]]; then
    if [[ -x $(command -v consul) && -x $(command -v jq) ]]; then
        minio_discovery=$(consul watch -type=service -service=minio | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')
    fi

    MINIO_ADDR="https://"${minio_discovery:-"localhost:9000"}
fi

MINIO_ALIAS="s3::"${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

artifact_path="$(ls "$scriptdir"/artifacts/*.pyz)"
artifact=$(basename -- "$artifact_path")

IFS='-' read -r name version <<< "${artifact%.pyz}"
config="${name}-config-${version}.tgz"


export NOMAD_VAR_company_name_surrounding_agent_artifact=${NOMAD_VAR_company_name_surrounding_agent_artifact:-"s3::${MINIO_ADDR}/artifacts/company-name-surrounding-agent/${artifact}"}
export NOMAD_VAR_company_name_surrounding_agent_artifact_checksum=${NOMAD_VAR_company_name_surrounding_agent_artifact_checksum:-"sha256:$(sha256sum "$artifact_path" | awk '{print $1}')"}
export NOMAD_VAR_company_name_surrounding_agent_version="$version"
export NOMAD_VAR_company_name_surrounding_agent_config=${NOMAD_VAR_company_name_surrounding_agent_config:-"s3::${MINIO_ADDR}/artifacts/company-name-surrounding-agent/${config}"}

cd "$scriptdir"
set -x
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/company-name-surrounding-agent

nomad job run "$@" company-name-surrounding-agent.nomad
