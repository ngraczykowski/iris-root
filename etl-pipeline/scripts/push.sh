#!/usr/bin/env bash
set -eu -o pipefail

git checkout master
git pull

VERSION=`cat setup.py | grep version | xargs `
VERSION=${VERSION#version=}
VERSION=${VERSION%,}

echo $VERSION

REMOTE_IMAGE_NAME="docker.repo.silenteight.com/etl-pipeline"

echo "Pushing image for " $REMOTE_IMAGE_NAME
docker tag "${REMOTE_IMAGE_NAME}:latest" "${REMOTE_IMAGE_NAME}:$VERSION"
docker push "${REMOTE_IMAGE_NAME}:$VERSION"
docker push "${REMOTE_IMAGE_NAME}:latest"

REMOTE_IMAGE_NAME="docker.repo.silenteight.com/etl-pipeline-service"

echo "Pushing image for " $REMOTE_IMAGE_NAME:$VERSION
docker tag "${REMOTE_IMAGE_NAME}:latest" "${REMOTE_IMAGE_NAME}:$VERSION"
docker push "${REMOTE_IMAGE_NAME}:$VERSION"
docker push "${REMOTE_IMAGE_NAME}:latest"
