#!/bin/bash
set -x -o pipefail
echo "Docker image version copied to AWS:" $INSTALLER_VERSION "build number:" $BUILD_NUMBER
skopeo copy docker://docker.repo.silenteight.com/ae/adjudication-engine-app:$INSTALLER_VERSION docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/adjudication-engine-app:$INSTALLER_VERSION
skopeo copy docker://docker.repo.silenteight.com/ae/adjudication-engine-app:latest docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/adjudication-engine-app:latest
