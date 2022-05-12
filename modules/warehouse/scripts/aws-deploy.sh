#!/bin/bash
set -x -o pipefail
aws ecr get-login-password --region us-east-1 | skopeo login --username AWS --password-stdin 278875966478.dkr.ecr.us-east-1.amazonaws.com
echo "Docker image version copied to AWS:" $INSTALLER_VERSION "build number:" $BUILD_NUMBER
skopeo copy docker://docker.repo.silenteight.com/warehouse/warehouse-app:$INSTALLER_VERSION docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/warehouse-app:$INSTALLER_VERSION
skopeo copy docker://docker.repo.silenteight.com/warehouse/warehouse-app:latest docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/warehouse-app:latest

skopeo copy docker://docker.repo.silenteight.com/warehouse/warehouse-es-init:$WAREHOUSE_ES_INIT_VERSION docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/warehouse-es-init:$WAREHOUSE_ES_INIT_VERSION
skopeo copy docker://docker.repo.silenteight.com/warehouse/warehouse-es-init:latest docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/warehouse-es-init:latest

skopeo copy docker://docker.repo.silenteight.com/warehouse/warehouse-sim-migration:$WAREHOUSE_SIM_MIGRATION_VERSION docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/warehouse-sim-migration:WAREHOUSE_SIM_MIGRATION_VERSION
skopeo copy docker://docker.repo.silenteight.com/warehouse/warehouse-sim-migration:latest docker://278875966478.dkr.ecr.us-east-1.amazonaws.com/warehouse-sim-migration:latest
