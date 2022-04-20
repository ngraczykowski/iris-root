#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -d venv ]]; then
  source venv/bin/activate
fi

# wheel, for developing and PyPi
python3 setup.py bdist_wheel
artifact=$(basename -- "$(ls ./dist/hit_type-*.whl)")
version=$(ls -al "./dist/$artifact" | awk -F'hit_type-|-py3-none-any.whl' '{print $2}')

# zipfile (executable, to run without installing)
pip install shiv
shiv -e hit_type.main:main "$@" --compressed -o "./dist/hit_type-$version.pyz" "dist/$artifact"

# configuration example
example_config_path="./config"
example_config="hit_type_config-${version}.tgz"
tar -cvzf "./dist/$example_config" "$example_config_path"
