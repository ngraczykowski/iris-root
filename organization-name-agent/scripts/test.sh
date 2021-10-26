#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -d venv ]]; then
  source venv/bin/activate
fi

artifact=$(basename -- "$(ls ./dist/company_name-*.whl)")
python -m tox --installpkg "./dist/$artifact" "$@"
