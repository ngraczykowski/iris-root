#!/usr/bin/env bash
set -e

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [ -d venv ]; then
  source venv/bin/activate
fi

artifact=$(basename -- "$(ls ./dist/agent_base-*.whl)")
python -m tox --installpkg "./dist/$artifact" "$@"
