#!/usr/bin/env bash
set -e

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"

PYTHON="python3"
if command -v "python3.7" &> /dev/null; then
    PYTHON="python3.7"
else
    echo "WARN: The command python3.7 does not exist, using python3 command instead"
fi

cd "$basedir"
if [ -d venv ]; then
  rm -rf venv
fi

"$PYTHON" -m venv venv
source venv/bin/activate

pip install "$@" -U pip setuptools wheel
pip install "$@" -r requirements.txt
pip install "$@" -e .
