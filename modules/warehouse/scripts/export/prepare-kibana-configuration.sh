#!/bin/bash
set -e -o pipefail
# set -o xtrace

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
SOURCE_INDEX="${3}"
TARGET_INDEX="${4}"
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")


# Load and process saved objects
SAVED_OBJECTS_EXPORT_FILE=$(find "${EXPORT_DIR}" -name "${TENANT}.*.so.ndjson" -print0 |
  xargs -r -0 ls -1 -t |
  head -1)

SAVED_OBJECTS_TMP_IMPORT_FILE=$(echo "${IMPORT_DIR}/${TARGET_TENANT}.${TIMESTAMP}.so.ndjson._tmp")

LINE_CONDITION_MATCHING='"type":"index-pattern"'
cat $SAVED_OBJECTS_EXPORT_FILE |
  sed "/${LINE_CONDITION_MATCHING}/s/${SOURCE_INDEX}/${TARGET_INDEX}/g" |
  base64 -w 0 \
  > ${SAVED_OBJECTS_TMP_IMPORT_FILE}


# Load and process report definitions
REPORTS_DEFINITIONS_EXPORT_FILE=$(find "${EXPORT_DIR}" -name "${TENANT}.*.rd.json" -print0 |
  xargs -r -0 ls -1 -t |
  head -1)

REPORTS_DEFINITIONS_TMP_IMPORT_FILE=$(echo "${IMPORT_DIR}/${TARGET_TENANT}.${TIMESTAMP}.rd.json._tmp")

cat "${REPORTS_DEFINITIONS_EXPORT_FILE}" |
  jq 'del( .data[]._source.report_definition.report_params.core_params.origin )' |
  base64 -w 0 \
  > ${REPORTS_DEFINITIONS_TMP_IMPORT_FILE}

# Prepare output
IMPORT_FILE=$(echo "${IMPORT_DIR}/${TARGET_TENANT}.${TIMESTAMP}.json")

jq -n \
  --arg target_tenant "${TARGET_TENANT}" \
  --rawfile saved_objects "${SAVED_OBJECTS_TMP_IMPORT_FILE}" \
  --rawfile report_instances "${REPORTS_DEFINITIONS_TMP_IMPORT_FILE}" \
  '{
    tenant: $target_tenant,
    saved_objects: $saved_objects,
    report_instances: $report_instances
  }' > ${IMPORT_FILE}
