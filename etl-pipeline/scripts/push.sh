#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline'

echo "Pushing image for " REMOTE_IMAGE_NAME
docker push "${REMOTE_IMAGE_NAME}:latest"


REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline-service:0.5.6-dev'

echo "Pushing image for " REMOTE_IMAGE_NAME
docker push "${REMOTE_IMAGE_NAME}"