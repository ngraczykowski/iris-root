#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"


company_name_agent_artifact_path="$(ls "$scriptdir"/../dist/company_name-*.pyz)"
company_name_agent_version=$(ls -al "$company_name_agent_artifact_path" | awk -F'company_name-|.pyz' '{print $2}')
company_name_agent_config="company_name_config-${company_name_agent_version}.tgz"
company_name_agent_config_path="$scriptdir/../dist/${company_name_agent_config}"

set -x


cd nomad

if [ -f "company-name-agent.nomad" ]; then
  echo "Processing nomad"
  rm -rf artifacts/
  mkdir -p artifacts/

  cp "$company_name_agent_artifact_path" artifacts/
  cp "$company_name_agent_config_path" artifacts/

  tar -cvjSf ../"company-name-agent-nomad-${company_name_agent_version}.tar.bz2" -- *
  cd -
else
  echo "No nomad file in nomad directory, ignoring"
  cd -
fi


