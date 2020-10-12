#!/bin/bash
set -x -o pipefail

PASSWORD="Password1!"

execute_serp() {
    "${SERP_HOME}"/bin/serp "$@"
}

if [[ ! -x "${SERP_HOME}/bin/serp" ]]; then
    echo "ERROR: bin/serp not available."
    exit 1
fi

execute_serp user create -u business_operator -d "Business Operator" -r "Business Operator" --password "${PASSWORD}"
execute_serp user create -u administrator -d "Administrator" -r "Administrator" --password "${PASSWORD}"
execute_serp user create -u approver -d "Approver" -r "Approver" --password "${PASSWORD}"
execute_serp user create -u analyst -d "Analyst" -r "Analyst" --password "${PASSWORD}"
execute_serp user create -u auditor -d "Auditor" -r "Auditor" --password "${PASSWORD}"
execute_serp user create -u superuser -d "Superuser" \
  -r "Administrator" -r "Business Operator" -r "Approver" -r "Analyst" -r "Auditor" --password "${PASSWORD}"