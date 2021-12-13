#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline'

echo "Pushing image for 'latest'"
docker push "${REMOTE_IMAGE_NAME}:latest"
