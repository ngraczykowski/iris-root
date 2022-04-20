#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"


hit_type_agent_artifact_path="$(ls "$scriptdir"/../dist/hit_type-*.pyz)"
hit_type_agent_version=$(ls -al "$hit_type_agent_artifact_path" | awk -F'hit_type-|.pyz' '{print $2}')
hit_type_agent_config="hit_type_config-${hit_type_agent_version}.tgz"
hit_type_agent_config_path="$scriptdir/../dist/${hit_type_agent_config}"

set -x


for d in nomad/*/; do
  cd "$d"
  name=$(basename -- "$d")

  if [ -f "hit-type-agent.nomad" ]; then
    echo "Processing $name"
  else
    echo "No nomad file in $name, ignoring"
    cd -
    continue
  fi

  rm -rf artifacts/
  mkdir -p artifacts/

  cp "$hit_type_agent_artifact_path" artifacts/
  cp "$hit_type_agent_config_path" artifacts/

  tar -cvjSf ../"hit-type-agent-nomad-${name}-${hit_type_agent_version}.tar.bz2" -- *
  cd -
done
