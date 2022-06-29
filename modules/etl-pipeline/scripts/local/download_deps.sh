#!/usr/bin/env bash
set -eu -o pipefail

export WHEELDIR="temp"

rm -R venv || true
python3.7 -m venv venv

source venv/bin/activate
rm -R $WHEELDIR  || true

mkdir -p  $WHEELDIR

# optional if there is a problem with pip
which pip
pip install --upgrade setuptools pip wheel
python setup.py bdist_wheel


pip wheel . -w $WHEELDIR --no-cache-dir
rm $WHEELDIR/etl*
artifact=$(basename -- "$(ls ./dist/etl_pipeline-*.whl)")
mv dist/$artifact $WHEELDIR/
