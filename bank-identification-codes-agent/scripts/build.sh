#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -d venv ]]; then
  source venv/bin/activate
fi

# wheel, for developing and PyPi
python setup.py bdist_wheel
artifact=$(basename -- "$(ls -tr ./dist/bank_identification_codes*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'bank_identification_codes-|-py3-none-any.whl' '{print $2}')

# zipfile (executable, to run without installing)
pip install shiv
shiv -e bank_identification_codes_agent.__main__:main "$@" --compressed \
-o "./dist/bank_identification_codes-$version.pyz" "dist/$artifact"

# configuration example
example_config_path="./config"
example_config="bank_identification_codes-config-${version}.tgz"
tar -cvzf "./dist/$example_config" "$example_config_path"
