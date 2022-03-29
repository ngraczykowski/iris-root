#!/usr/bin/env bash
set -eu -o pipefail

rm -R venv || true
python3.6 -m venv venv

source venv/bin/activate
rm -R $WHEELDIR  || true

mkdir -p  $WHEELDIR

# optional if there is a problem with pip
# curl https://bootstrap.pypa.io/pip/3.6/get-pip.py | python - 
pip wheel etl_pipeline -w $WHEELDIR --no-cache-dir
