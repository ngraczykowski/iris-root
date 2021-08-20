#!/usr/bin/env bash
set -e

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [ -d venv ]; then
  source venv/bin/activate
fi

python setup.py bdist_wheel
artifact=$(basename -- "$(ls ./dist/company_name-*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'company_name-|-py3-none-any.whl' '{print $2}')

pip install shiv
shiv -e company_name.main:main "$@" --compressed -o "./dist/company_name-$version.pyz" "dist/$artifact"