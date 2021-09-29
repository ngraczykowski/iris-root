#!/bin/bash
set -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

version=$INSTALLER_VERSION
helm_repo="s8helm"

if [ -z "${INSTALLER_VERSION}" ]; then
    echo "INSTALLER_VERSION is not set please use env.INSTALLER_VERSION = utils.getProjectVersion()"
    exit 1;
fi

mkdir -p build/chart/
helm package --app-version $version --destination build/chart helm-chart
simulator_app_artifact=$(basename -- "$(ls "$scriptdir"/../build/chart/simulator-app-*.tgz)")
echo "Creating HELM chart: $simulator_app_artifact"
helm push-artifactory --skip-reindex $scriptdir/../build/chart/$simulator_app_artifact $helm_repo
