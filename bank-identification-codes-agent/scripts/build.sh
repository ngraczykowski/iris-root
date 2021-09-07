#!/usr/bin/env bash
set -e

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [ -d venv ]; then
    source venv/bin/activate
fi


python setup.py bdist_wheel
artifact=$(basename -- "$(ls -tr ./dist/*.whl)")
IFS='-' read -r name version rest <<< "$artifact"

# wheel
pip install tox
python -m tox --installpkg ./dist/"$artifact" "$@"

# zipfile (executable, to run without installing)
pip install shiv
shiv -e idmismatchagent.__main__:main "$@" --compressed -o "./dist/$name-$version.pyz" "dist/$artifact"

# configuration example
example_config_path="./config"
example_config="${name}-${version}-config.tgz"
tar -cvzf "./dist/$example_config" "$example_config_path"
