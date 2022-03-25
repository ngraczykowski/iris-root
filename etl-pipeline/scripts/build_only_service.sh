#!/usr/bin/env bash
set -eu -o pipefail
echo "Docker image ready"

echo "Building docker image for etl service"
docker build -f docker/service/prod/Dockerfile --build-arg PIP_INDEX_URL=$PIP_INDEX_URL -t docker.repo.silenteight.com/etl-pipeline-service . 
echo "Docker image ready"