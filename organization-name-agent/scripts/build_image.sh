#!/usr/bin/env bash
set -eu -o pipefail

echo "Building docker image"
echo $PYPI_URL > pip_index_url
DOCKER_BUILDKIT=1 docker build -t docker.repo.silenteight.com/organization-name-agent --secret id=mysecret,src=pip_index_url .
rm pip_index_url
echo "Docker image ready"
