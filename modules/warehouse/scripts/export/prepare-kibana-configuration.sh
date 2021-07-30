#!/bin/bash
set -e -o pipefail

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

# load settings from .env file
set -a
[ -f .env ] && . .env
set +a


# e.g. ./prepare-kibana-configuration local_production_ai_reasoning dev_production_ai_reasoning local_ dev_
TENANT="${1}"
TARGET_TENANT="${2}"
SOURCE_PREFIX="${3}"
TARGET_PREFIX="${4}"
TIMESTAMP=$(date +"%Y%d%m-%H%M%S")

# Load and process saved objects
SAVED_OBJECTS_IMPORT_FILE=$(find "${EXPORT_DIR}" -name "${TENANT}.*.so.ndjson" -print0 |
  xargs -r -0 ls -1 -t |
  head -1)

LINE_CONDITION_MATCHING='"type":"index-pattern"'
SAVED_OBJECTS_PAYLOAD=$(cat $SAVED_OBJECTS_IMPORT_FILE |
  sed "/${LINE_CONDITION_MATCHING}/s/${SOURCE_PREFIX}/${TARGET_PREFIX}/g" |
  base64 -w 0)


# Load and process report definitions
REPORTS_DEFINITIONS_IMPORT_FILE=$(find "${EXPORT_DIR}" -name "${TENANT}.*.rd.json" -print0 |
  xargs -r -0 ls -1 -t |
  head -1)

REPORT_INSTANCES_PAYLOAD=$( cat "${REPORTS_DEFINITIONS_IMPORT_FILE}" |
  jq 'del( .data[]._source.report_definition.report_params.core_params.origin )' |
  base64 -w 0 )

# Prepare output
EXPORT_FILE=$(echo "${IMPORT_DIR}/${TARGET_TENANT}.${TIMESTAMP}.json")

jq -n \
  --arg target_tenant "${TARGET_TENANT}" \
  --arg saved_objects "${SAVED_OBJECTS_PAYLOAD}" \
  --arg report_instances "${REPORT_INSTANCES_PAYLOAD}" \
  '{
    tenant: $target_tenant,
    saved_objects: $saved_objects,
    report_instances: $report_instances
  }' > ${EXPORT_FILE}
