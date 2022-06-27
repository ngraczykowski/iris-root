#!/bin/bash
#
# Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
#

required_var(){
  VARNAME=$1
  if [ x"${!VARNAME}" == "x" ]; then
       echo "environment variable '${VARNAME}' is required"
       exit 1
    fi
}

export LOGS_DIR=logs/${ENVIRONMENT}
export LOGS_PREFIX=${LOGS_DIR}/${ENVIRONMENT}

required_var ENVIRONMENT
required_var NAMESPACE

if [ -t 0 ]
then
    cat << EOH
This script will:
 * create directory in current working dir ($LOGS_DIR)
 * dump logs from all pods in k8s ns ${NAMESPACE}

Do you want to proceed ? (CTRL-C to abort, ENTER - YES)
EOH
    read
fi

mkdir -p "${LOGS_DIR}"

kubectl get pods \
 --namespace ${NAMESPACE} \
 --output wide \
 --sort-by '.status.containerStatuses[0].restartCount' | tee "${LOGS_PREFIX}_pods.log"

kubectl get events \
 --namespace ${NAMESPACE} \
 --sort-by='.metadata.creationTimestamp' | tee "${LOGS_PREFIX}_events.log"

FAILED_PODS=$(set +x && kubectl get pods \
 --namespace ${NAMESPACE} \
 --output=custom-columns="POD:metadata.name" \
 --no-headers)
for pod in ${FAILED_PODS} ; do
  kubectl --namespace ${NAMESPACE} logs --previous $pod >${LOGS_PREFIX}_${pod}.previous.log || true
  kubectl --namespace ${NAMESPACE} logs ${pod} >${LOGS_PREFIX}_${pod}.log || true
done
