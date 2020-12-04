#!/usr/bin/env bash
set -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

cd "$scriptdir"/..

RABBITMQ_CONTAINER=serp_rabbitmq_1
RABBITMQ_USER=serp
RABBITMQ_PASSWORD=serp

# Source `.env` file from repository root if it exists.
[ -f .env ] && source .env

echo_ts() {
  echo "$(date --rfc-3339=seconds)" "$@"
}

rabbitmqctl() {
  args="$*"
  safe_args="${args/${RABBITMQ_PASSWORD}/<redacted>}"
  echo_ts "--- rabbitmqctl" "$safe_args"
  docker exec $RABBITMQ_CONTAINER rabbitmqctl "$@"
}

# Import exchange and queue definitions from JSON file
docker cp rabbitmq/definitions.json $RABBITMQ_CONTAINER:/etc/rabbitmq/
rabbitmqctl import_definitions --format json /etc/rabbitmq/definitions.json
