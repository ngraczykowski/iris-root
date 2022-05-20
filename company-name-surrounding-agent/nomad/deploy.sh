#!/usr/bin/env bash
set  -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com

MINIO_ADDR="s3::"${MINIO_ADDR}

MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

agent_artifact_path="$(ls "$scriptdir"/artifacts/company_name_surrounding_agent-*.pyz)"
agent_artifact=$(basename -- "$agent_artifact_path")
agent_version=$(ls -al "$agent_artifact_path" | awk -F'company_name_surrounding_agent-|.pyz' '{print $2}')
agent_config="company_name_surrounding_agent-config-${agent_version}.tgz"


export NOMAD_VAR_company_name_surrounding_agent_artifact=${NOMAD_VAR_company_name_surrounding_agent_artifact:-"${MINIO_ADDR}/artifacts/company-name-surrounding-agent/${agent_artifact}"}
export NOMAD_VAR_company_name_surrounding_agent_artifact_checksum=${NOMAD_VAR_company_name_surrounding_agent_artifact_checksum:-"sha256:$(sha256sum "$agent_artifact_path" | awk '{print $1}')"}
export NOMAD_VAR_company_name_surrounding_agent_version="$agent_version"
export NOMAD_VAR_company_name_surrounding_agent_config=${NOMAD_VAR_company_name_surrounding_agent_config:-"${MINIO_ADDR}/artifacts/company-name-surrounding-agent/${agent_config}"}

cd "$scriptdir"
set -x
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/company-name-surrounding-agent

nomad job run "$@" company-name-surrounding-agent.nomad
