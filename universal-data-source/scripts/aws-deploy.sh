#!/bin/bash
set -x -o pipefail
export AWS_PROFILE=sierra-uat
export AWS_REGION=eu-central-1
export EKS_NAME=sierra-uat-eks
export NAMESPACE=default
export ECR_REPO=278875966478.dkr.ecr.us-east-1.amazonaws.com
export CHART_NAME=universal-data-source-app
export IMAGE_REPO=$ECR_REPO/$CHART_NAME

echo "Docker image version copied to AWS:" $INSTALLER_VERSION "build number:" $BUILD_NUMBER
aws ecr get-login-password --region us-east-1 | skopeo login --username AWS --password-stdin $ECR_REPO
skopeo copy docker://docker.repo.silenteight.com/uds/$CHART_NAME:$INSTALLER_VERSION docker://$IMAGE_REPO:$INSTALLER_VERSION
skopeo copy docker://docker.repo.silenteight.com/uds/$CHART_NAME:$INSTALLER_VERSION docker://$IMAGE_REPO:latest

