#!/usr/bin/env bash
set -xeu -o pipefail
DIR="$(cd -- "$(dirname -- "${0}")" && pwd)"
export ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"
export SERP_HOME="${SERP_HOME:-$ROOT_DIR/env/serp}"
export PATH="$ROOT_DIR/env/ds/opt/jdk/bin:$SERP_HOME/bin:$ROOT_DIR/env/ds/bin:$ROOT_DIR/env/ds/opt/anaconda/bin:$PATH"

echo "Starting agents..."

#todo (use daemon approach with writing logs to file)
find "$ROOT_DIR/env/agents/" -name *.jar | xargs -I % sh -c '{ nohup java -jar % &  }'
