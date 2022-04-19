#!/bin/bash
set -x -o pipefail
export ECR_REPO=278875966478.dkr.ecr.us-east-1.amazonaws.com
export CHART_NAME=governance-app
export IMAGE_REPO=$ECR_REPO/$CHART_NAME

echo "Docker image version copied to AWS:" $INSTALLER_VERSION "build number:" $BUILD_NUMBER
aws ecr get-login-password --region us-east-1 | skopeo login --username AWS --password-stdin $ECR_REPO
skopeo copy docker://docker.repo.silenteight.com/governance/$CHART_NAME:$INSTALLER_VERSION docker://$IMAGE_REPO:$INSTALLER_VERSION
skopeo copy docker://docker.repo.silenteight.com/governance/$CHART_NAME:$INSTALLER_VERSION docker://$IMAGE_REPO:latest
