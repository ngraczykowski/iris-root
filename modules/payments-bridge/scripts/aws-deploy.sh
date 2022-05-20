#!/bin/bash
set -x -o pipefail
export ECR_REPO=278875966478.dkr.ecr.us-east-1.amazonaws.com
export CHART_NAME=sear-payments-bridge-app
export IMAGE_REPO=$ECR_REPO/$CHART_NAME
export IMAGE_SOURCE=docker://docker.repo.silenteight.com/pb/$CHART_NAME:$INSTALLER_VERSION
export ECR_DESTINATION=docker://$IMAGE_REPO
echo "Docker image version copied to AWS:" $INSTALLER_VERSION "build number:" $BUILD_NUMBER
aws ecr get-login-password --region us-east-1 | skopeo login --username AWS --password-stdin $ECR_REPO

skopeo copy docker://docker.repo.silenteight.com/pb/$CHART_NAME:$INSTALLER_VERSION $ECR_DESTINATION:$INSTALLER_VERSION
skopeo copy docker://docker.repo.silenteight.com/pb/$CHART_NAME:snapshot  $ECR_DESTINATION:snapshot
skopeo copy docker://docker.repo.silenteight.com/pb/$CHART_NAME:latest  $ECR_DESTINATION:latest

