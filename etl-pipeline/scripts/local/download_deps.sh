#!/usr/bin/env bash
set -eu -o pipefail

rm -R venv 2>/dev/null
python3.6 -m venv venv

source venv/bin/activate
rm -R $WHEELDIR 2>/dev/null
mkdir -p  $WHEELDIR
curl https://bootstrap.pypa.io/pip/3.6/get-pip.py | python -
echo `which python`
pip wheel etl_pipeline -w $WHEELDIR --no-cache-dir