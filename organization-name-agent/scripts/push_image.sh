#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/organization-name-agent'

package_version=`cat PACKAGE_VERSION`
commit_id=$(git rev-parse --short HEAD)
image_tag="${package_version}-${commit_id}"

echo "Tagging and pushing image for 'latest'"
docker tag "${REMOTE_IMAGE_NAME}" "${REMOTE_IMAGE_NAME}:latest"
docker push "${REMOTE_IMAGE_NAME}:latest"

echo "Tagging and pushing image for $image_tag"
docker tag "${REMOTE_IMAGE_NAME}" "${REMOTE_IMAGE_NAME}:${image_tag}"
docker push "${REMOTE_IMAGE_NAME}:${image_tag}"
