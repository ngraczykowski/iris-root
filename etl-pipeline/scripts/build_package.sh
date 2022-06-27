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
artifact=$(basename -- "$(ls ./dist/etl_pipeline-*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'etl_pipeline-|-py3-none-any.whl' '{print $2}')

# zipfile (executable, to run without installing)
pip install shiv
shiv -e etl_pipeline.__main__:main "$@" --compressed -o "./dist/etl_pipeline-$version.pyz" "dist/$artifact"
