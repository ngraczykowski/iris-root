function issue_gnsrt_request_to_serp_and_sear() {

  IN_FILE=$1

  echo "Issuing gns rt request from file $IN_FILE"

  echo "Request length: $(stat -c%s "$IN_FILE")"

  echo "-- Sending request to SEAR"
  issue_gnsrt_request $IN_FILE $SEAR_SCB_BRIDGE_ADDR ${IN_FILE}.sear.out.json

  echo "-- Sending request to SERP"
  issue_gnsrt_request $IN_FILE $SERP_SCB_BRIDGE_ADDR ${IN_FILE}.serp.out.json

  echo "-- Comparing outputs"
  diff ${IN_FILE}.sear.out.json ${IN_FILE}.serp.out.json >${IN_FILE}.diff || true

  DIFF_LEN=$(stat -c%s "${IN_FILE}.diff")
  if [ $DIFF_LEN -eq 0 ]; then
    echo "OK - responses are the same"
  else
    echo "FAILED - responses are not the same, diff length: $DIFF_LEN"
  fi
}

function issue_gnsrt_request() {
  IN_FILE=$1
  ADDR=$2
  OUT_FILE=$3

  echo "Sending request from: $IN_FILE to: $ADDR and save to: $OUT_FILE"

  start_time=$(date +%s)

  curl -s -S -k -X POST $ADDR/v1/gnsrt/recommendation -H 'Content-Type: application/json' -d @${IN_FILE} | jq "." >$OUT_FILE

  end_time=$(date +%s)

  echo "Elapsed time: $((end_time - start_time)) sec"
  echo "File length: $(stat -c%s "$OUT_FILE")"

}
