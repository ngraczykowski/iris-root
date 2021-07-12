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

execute_serp user create -u model_tuner -d "Model Tuner" -r "Model Tuner" --password "${PASSWORD}"
execute_serp user create -u approver -d "Approver" -r "Approver" --password "${PASSWORD}"
execute_serp user create -u auditor -d "Auditor" -r "Auditor" --password "${PASSWORD}"
execute_serp user create -u user_administrator -d "User Administrator" -r "User Administrator" --password "${PASSWORD}"
execute_serp user create -u qa -d "QA" -r "QA" --password "${PASSWORD}"
execute_serp user create -u qa_issue_manager -d "QA Issue Manager" -r "QA Issue Manager" --password "${PASSWORD}"
execute_serp user create -u superuser -d "Superuser" \
  -r "Model Tuner" -r "Approver" -r "Auditor" -r "User Administrator" \
  -r "QA" -r "QA Issue Manager" --password "${PASSWORD}"