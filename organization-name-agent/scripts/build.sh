#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [ -d venv ]; then
  source venv/bin/activate
fi

# wheel, for developing and PyPi
python setup.py bdist_wheel
artifact=$(basename -- "$(ls ./dist/company_name-*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'company_name-|-py3-none-any.whl' '{print $2}')

# zipfile (executable, to run without installing)
pip install shiv
shiv -e company_name.main:main "$@" --compressed -o "./dist/company_name-$version.pyz" "dist/$artifact"

# configuration example
example_config_path="./config"
example_config="company_name_config-${version}.tgz"
tar -cvzf "./dist/$example_config" "$example_config_path"
