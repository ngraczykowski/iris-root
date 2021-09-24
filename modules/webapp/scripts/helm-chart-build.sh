#!/bin/bash
set -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

version=$INSTALLER_VERSION
helm_repo="s8helm"

if [ -z "${INSTALLER_VERSION}" ]; then
    echo "INSTALLER_VERSION is not set please use env.INSTALLER_VERSION = utils.getProjectVersion()"
    exit 1;
fi

if [ -z "${1}" ]; then
    echo "Repository username is not set as first parameter"
    exit 1;
fi

if [ -z "${2}" ]; then
    echo "Repository password is not set as second parameter"
    exit 1;
fi

mkdir -p build/chart/
helm package --app-version $version --destination build/chart helm-chart
webapp_app_artifact=$(basename -- "$(ls "$scriptdir"/../build/chart/webapp-app-*.tgz)")
echo "Creating HELM chart: $webapp_app_artifact"
helm push-artifactory --skip-reindex $scriptdir/../build/chart/$webapp_app_artifact $helm_repo
