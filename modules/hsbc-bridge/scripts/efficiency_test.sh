echo "Bash version: $BASH_VERSION"

# Description in README.md
# Settings.
runtime="3 minutes"
pause_between_check_status_requests=10
path_to_json="./efficiency_test.json"
request_url="localhost:24802/async/batch/v1/"

# Don't change below variables.
completed_batches=0
end_time=$(date -ud "$runtime" +%s)

check_batch_completed_status() {
  while true; do
    response=$(curl -s -H 'Content-Type: application/json' "$request_url""$batch_id"/status | jq '.batchStatus')
    if [[ "$response" == "\"COMPLETED\"" ]]; then
      echo "$(date +%H:%M:%S) Batch with batchId: $batch_id is \"COMPLETED\""
      return
    fi
    sleep $pause_between_check_status_requests
  done
}

while [[ $(date -u +%s) -le $end_time ]]; do
  batch_id=test-"$(date +%H:%M:%S)"
  echo "$(date +%H:%M:%S) Sending request for batchId: $batch_id"
  curl -H 'Content-Type: application/json' --data @$path_to_json "$request_url""$batch_id"/recommend
  echo
  check_batch_completed_status
  ((completed_batches++))
  echo
done
echo -e "\n$completed_batches batches completed in $runtime"
