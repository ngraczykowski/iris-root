#!/usr/bin/env bash
set -eu -o pipefail

echo "Building docker image"
docker pull docker.repo.silenteight.com/ds-anaconda
docker-compose -f docker/prod/docker-compose.yaml build --no-cache
docker-compose -f docker/test/docker-compose-test.yaml build --no-cache
echo "Docker image ready"
