#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -d venv ]]; then
  source venv/bin/activate
fi

artifact=$(basename -- "$(ls ./dist/bank_identification_codes-*.whl)")

python3 -m tox --installpkg "./dist/$artifact" "$@"
