#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"


company_name_agent_artifact_path="$(ls "$scriptdir"/../dist/company_name-*.pyz)"
company_name_agent_version=$(ls -al "$company_name_agent_artifact_path" | awk -F'company_name-|.pyz' '{print $2}')
company_name_agent_config_path=$(realpath "$scriptdir"/../config)
company_name_agent_config="company_name_agent_config_${company_name_agent_version}.tgz"

set -x


for d in nomad/*/; do
  cd $d
  name=$(basename -- "$d")

  rm -rf config/
  mkdir -p config/
  cp -r "$company_name_agent_config_path"/* ./config/
  cp application.nomad.yaml ./config/
  tar -cvzf "$company_name_agent_config" ./config
  rm -rf config/

  rm -rf artifacts/
  mkdir -p artifacts/

  mv "${company_name_agent_config}" artifacts/
  cp "$company_name_agent_artifact_path" artifacts/

  tar -cvjSf ../"company-name-agent-nomad-${name}-${company_name_agent_version}.tar.bz2" -- *
  cd -
done
