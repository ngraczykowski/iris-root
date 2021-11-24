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
artifact=$(basename -- "$(ls ./dist/company_name_surrounding_agent-*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'company_name_surrounding_agent-|-py3-none-any.whl' '{print $2}')

# zipfile (executable, to run without installing)
pip install shiv
shiv -e company_name_surrounding.__main__:main "$@" --compressed -o "./dist/company_name_surrounding_agent-$version.pyz" "dist/$artifact"

# configuration example
example_config_path="./config"
example_config="company_name_surrounding_agent-config-${version}.tgz"
tar -cvzf "./dist/$example_config" "$example_config_path"
