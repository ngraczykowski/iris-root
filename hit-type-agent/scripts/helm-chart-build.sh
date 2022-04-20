#!/bin/bash
set -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

helm_repo="s8helm"

helm repo add --username "$DEPLOY_USERNAME" --password "$DEPLOY_PASSWORD" $helm_repo https://repo.silenteight.com/artifactory/helm
mkdir -p build/chart/
helm package --app-version ${VERSION} --destination build/chart helm-chart
hit_type_agent_artifact=$(basename -- "$(ls "$scriptdir"/build/chart/hit-type-agent-*.tgz)")
echo "Creating HELM chart: $hit_type_agent_artifact"
helm push-artifactory --skip-reindex $scriptdir/build/chart/$hit_type_agent_artifact $helm_repo
