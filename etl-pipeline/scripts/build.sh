#!/usr/bin/env bash
set -eu -o pipefail

echo "Building docker image"
docker-compose -f docker/docker-compose.yaml build
echo "Docker image ready"
