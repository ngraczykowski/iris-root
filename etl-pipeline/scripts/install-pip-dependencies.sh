#!/usr/bin/env bash
set -xeu -o pipefail
ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"
export PATH="$ROOT_DIR/env/ds/opt/anaconda/bin:$PATH"

if [[ ! -f "$ROOT_DIR/.prod" ]]; then
    pip install -r "$ROOT_DIR/requirements.txt"
elif [[ ! -d "$ROOT_DIR/env/installer/pip-dependencies" ]]; then
    echo "ERROR: Missing PIP dependencies in $ROOT_DIR/build/pip-dependencies"
    exit 1
else
    pip install -r "$ROOT_DIR/requirements.txt" \
        --no-index \
        --find-links "file:$ROOT_DIR/env/installer/pip-dependencies"
fi
