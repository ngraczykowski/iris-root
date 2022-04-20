#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

if [[ -z "$MINIO_ADDR" ]]; then
    if [[ -x $(command -v consul) && -x $(command -v jq) ]]; then
        minio_discovery=$(consul watch -type=service -service=minio | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')
    fi

    MINIO_ADDR="http://"${minio_discovery:-"localhost:9000"}
fi

MINIO_ADDR="s3::"${MINIO_ADDR}

MINIO_ALIAS=${MINIO_ALIAS:-minio}

export NOMAD_ADDR="${NOMAD_ADDR:-http://localhost:4646}"

hit_type_agent_artifact_path="$(ls "$scriptdir"/artifacts/hit_type-*.pyz)"
hit_type_agent_artifact=$(basename -- "$hit_type_agent_artifact_path")
hit_type_agent_version=$(ls -al "$hit_type_agent_artifact_path" | awk -F'hit_type-|.pyz' '{print $2}')
hit_type_agent_config="hit_type_config-${hit_type_agent_version}.tgz"


export NOMAD_VAR_hit_type_agent_artifact=${NOMAD_VAR_hit_type_agent_artifact:-"${MINIO_ADDR}/artifacts/hit-type-agent/${hit_type_agent_artifact}"}
export NOMAD_VAR_hit_type_agent_artifact_checksum=${NOMAD_VAR_hit_type_agent_artifact_checksum:-"sha256:$(sha256sum "$hit_type_agent_artifact_path" | awk '{print $1}')"}
export NOMAD_VAR_hit_type_agent_version="$hit_type_agent_version"
export NOMAD_VAR_hit_type_agent_config=${NOMAD_VAR_hit_type_agent_config:-"${MINIO_ADDR}/artifacts/hit-type-agent/${hit_type_agent_config}"}

cd "$scriptdir"
set -x
mcli cp --recursive artifacts/ "$MINIO_ALIAS"/artifacts/hit-type-agent

nomad job run "$@" hit-type-agent.nomad


# uncomment for run on local nomad in docker instead of command above
# tricky - needs docker in docker or bound sockets and paths configured

#docker exec -i nomad_s1_1 nomad job run \
# -var hit_type_agent_artifact="$NOMAD_VAR_hit_type_agent_artifact" \
# -var hit_type_agent_version="$NOMAD_VAR_hit_type_agent_version" \
# -var hit_type_agent_artifact_checksum="$NOMAD_VAR_hit_type_agent_artifact_checksum" \
# -var hit_type_agent_config="$NOMAD_VAR_hit_type_agent_config" \
# jobs/hit-type-agent.nomad