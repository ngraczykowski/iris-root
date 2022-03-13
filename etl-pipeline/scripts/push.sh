#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline'

echo "Tagging and pushing image for 'latest'"
docker push "${REMOTE_IMAGE_NAME}:latest"


REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline-service-0.5.5'

echo "Tagging and pushing image for 'latest'"
docker push "${REMOTE_IMAGE_NAME}"