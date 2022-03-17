#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME="docker.repo.silenteight.com/etl-pipeline:$1"

echo "Pushing image for " $REMOTE_IMAGE_NAME
docker push "${REMOTE_IMAGE_NAME}:$1"
docker tag "${REMOTE_IMAGE_NAME}:$1" "${REMOTE_IMAGE_NAME}:latest"
docker push "${REMOTE_IMAGE_NAME}:latest"

REMOTE_IMAGE_NAME="docker.repo.silenteight.com/etl-pipeline-service:$1"

echo "Pushing image for " $REMOTE_IMAGE_NAME
docker push "${REMOTE_IMAGE_NAME}:$1"
docker tag "${REMOTE_IMAGE_NAME}:$1" "${REMOTE_IMAGE_NAME}:latest"
docker push "${REMOTE_IMAGE_NAME}:latest"
