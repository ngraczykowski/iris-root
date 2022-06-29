#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

MINIO_ADDR=https://minio.silenteight.com

MINIO_ADDR="s3::"${MINIO_ADDR}

MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

company_name_agent_artifact_path="$(ls "$scriptdir"/artifacts/company_name-*.pyz)"
company_name_agent_artifact=$(basename -- "$company_name_agent_artifact_path")
company_name_agent_version=$(ls -al "$company_name_agent_artifact_path" | awk -F'company_name-|.pyz' '{print $2}')
company_name_agent_config="company_name_config-${company_name_agent_version}.tgz"


export NOMAD_VAR_company_name_agent_artifact=${NOMAD_VAR_company_name_agent_artifact:-"${MINIO_ADDR}/artifacts/company-name-agent/${company_name_agent_artifact}"}
export NOMAD_VAR_company_name_agent_artifact_checksum=${NOMAD_VAR_company_name_agent_artifact_checksum:-"sha256:$(sha256sum "$company_name_agent_artifact_path" | awk '{print $1}')"}
export NOMAD_VAR_company_name_agent_version="$company_name_agent_version"
export NOMAD_VAR_company_name_agent_config=${NOMAD_VAR_company_name_agent_config:-"${MINIO_ADDR}/artifacts/company-name-agent/${company_name_agent_config}"}

cd "$scriptdir"
set -x
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/company-name-agent

nomad job run "$@" company-name-agent.nomad


# uncomment for run on local nomad in docker instead of command above
# tricky - needs docker in docker or bound sockets and paths configured

#docker exec -i nomad_s1_1 nomad job run \
# -var company_name_agent_artifact="$NOMAD_VAR_company_name_agent_artifact" \
# -var company_name_agent_version="$NOMAD_VAR_company_name_agent_version" \
# -var company_name_agent_artifact_checksum="$NOMAD_VAR_company_name_agent_artifact_checksum" \
# -var company_name_agent_config="$NOMAD_VAR_company_name_agent_config" \
# jobs/company-name-agent.nomad
