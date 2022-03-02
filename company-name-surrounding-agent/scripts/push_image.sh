#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/company-name-surrounding-agent'

package_version=`cat PACKAGE_VERSION`

echo "Tagging and pushing image for 'latest'"
docker tag "${REMOTE_IMAGE_NAME}" "${REMOTE_IMAGE_NAME}:latest"
docker push "${REMOTE_IMAGE_NAME}:latest"

echo "Tagging and pushing image for $package_version"
docker tag "${REMOTE_IMAGE_NAME}" "${REMOTE_IMAGE_NAME}:${package_version}"
docker push "${REMOTE_IMAGE_NAME}:${package_version}"