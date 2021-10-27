#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"

if command -v "python3.7"; then
    PYTHON="python3.7"
else
    echo "WARN: The command python3.7 does not exist, installing python3.7"
    apt install python3.7
    PYTHON="python3.7"
fi

cd "$basedir"
if [[ -d venv ]]; then
  rm -rf venv
fi

"$PYTHON" -m venv venv
source venv/bin/activate

pip install "$@" --upgrade pip setuptools wheel
pip install "$@" --requirement requirements.txt
pip install "$@" --editable ".[tests]"
