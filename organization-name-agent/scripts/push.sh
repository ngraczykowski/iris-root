#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/organization-name-agent'
IMAGE_VERSION=`cat IMAGE_VERSION`

echo "Tagging and pushing image for 'latest'"
docker tag "${REMOTE_IMAGE_NAME}" "${REMOTE_IMAGE_NAME}":latest
docker push "${REMOTE_IMAGE_NAME}:latest"

echo "Tagging and pushing image for '${IMAGE_VERSION}'"
docker tag "${REMOTE_IMAGE_NAME}" "${REMOTE_IMAGE_NAME}:${IMAGE_VERSION}"
docker push "${REMOTE_IMAGE_NAME}:${IMAGE_VERSION}"
