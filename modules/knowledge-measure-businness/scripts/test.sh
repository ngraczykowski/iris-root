#!/usr/bin/env bash
set -eu -o pipefail

if [[ -d venv ]]; then
  source venv/bin/activate
fi

python3 -m tox "$@"
