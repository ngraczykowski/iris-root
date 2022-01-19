#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/organization-name-knowledge'

echo "Tagging and pushing image for 'latest'"
docker push "${REMOTE_IMAGE_NAME}:latest"
