#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"


hit_type_agent_artifact_path="$(ls "$scriptdir"/../dist/hit_type-*.pyz)"
hit_type_agent_version=$(ls -al "$hit_type_agent_artifact_path" | awk -F'hit_type-|.pyz' '{print $2}')
hit_type_agent_config="hit_type_config-${hit_type_agent_version}.tgz"
hit_type_agent_config_path="$scriptdir/../dist/${hit_type_agent_config}"

set -x


cd nomad

if [ -f "hit-type-agent.nomad" ]; then
  echo "Processing nomad"
  rm -rf artifacts/
  mkdir -p artifacts/

  cp "$hit_type_agent_artifact_path" artifacts/
  cp "$hit_type_agent_config_path" artifacts/

  tar -cvjSf ../"hit-type-agent-nomad-${hit_type_agent_version}.tar.bz2" -- *
  cd -
else
  echo "No nomad file in nomad directory, ignoring"
  cd -
fi


