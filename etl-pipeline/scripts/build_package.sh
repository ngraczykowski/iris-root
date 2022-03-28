#!/usr/bin/env bash
set  -e -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -d venv ]]; then
  source venv/bin/activate
fi

# wheel, for developing and PyPi
python setup.py bdist_wheel
artifact=$(basename -- "$(ls ./dist/etl-pipeline-*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'etl-pipeline-|-py3-none-any.whl' '{print $2}')
  