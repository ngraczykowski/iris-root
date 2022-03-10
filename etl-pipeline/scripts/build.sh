#!/usr/bin/env bash
set -eu -o pipefail

echo "Building docker image for playground"
docker-compose -f docker/prod/docker-compose.yaml build
docker-compose -f docker/test/docker-compose-test.yaml build

echo "Docker image ready"
