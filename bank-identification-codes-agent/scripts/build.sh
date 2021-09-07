#!/usr/bin/env bash
set -e

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [ -d venv ]; then
    source venv/bin/activate
fi


python setup.py bdist_wheel
tox --installpkg ./dist/*.whl "$@"
