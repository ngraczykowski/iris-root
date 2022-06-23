#!/usr/bin/env bash
set  -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com

MINIO_ADDR="s3::"${MINIO_ADDR}

MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

artifact_path="$(ls "$scriptdir"/artifacts/*.pyz)"
artifact=$(basename -- "$artifact_path")

IFS='-' read -r name version <<< "${artifact%.pyz}"
config="${name}-config-${version}.tgz"


export NOMAD_VAR_bank_identification_codes_agent_artifact=${NOMAD_VAR_bank_identification_codes_agent_artifact:-"${MINIO_ADDR}/artifacts/bank-identification-codes-agent/${artifact}"}
export NOMAD_VAR_bank_identification_codes_agent_artifact_checksum=${NOMAD_VAR_bank_identification_codes_agent_artifact_checksum:-"sha256:$(sha256sum "$artifact_path" | awk '{print $1}')"}
export NOMAD_VAR_bank_identification_codes_agent_version="$version"
export NOMAD_VAR_bank_identification_codes_agent_config=${NOMAD_VAR_bank_identification_codes_agent_config:-"${MINIO_ADDR}/artifacts/bank-identification-codes-agent/${config}"}

cd "$scriptdir"
set -x
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/bank-identification-codes-agent

nomad job run "$@" bank-identification-codes-agent.nomad