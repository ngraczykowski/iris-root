#!/usr/bin/env bash
set -eu -o pipefail

REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline'

echo "Tagging and pushing image for 'latest'"
docker push "${REMOTE_IMAGE_NAME}:latest"


REMOTE_IMAGE_NAME='docker.repo.silenteight.com/etl-pipeline-service'

echo "Tagging and pushing image for 'latest'"
docker push "${REMOTE_IMAGE_NAME}:latest"