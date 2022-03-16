#!/usr/bin/env bash
set -eu -o pipefail

echo "Building docker image for playground"
docker-compose -f docker/prod/docker-compose.yaml build
docker-compose -f docker/test/docker-compose-test.yaml build

echo "Docker image ready"

echo "Building docker image for etl service"
docker build -f docker/service/prod/Dockerfile --build-arg PIP_INDEX_URL=$PIP_INDEX_URL -t docker.repo.silenteight.com/etl-pipeline-service:0.5.6-dev . 
echo "Docker image ready"