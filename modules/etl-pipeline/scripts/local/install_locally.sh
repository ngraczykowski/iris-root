#!/usr/bin/env bash
set -eu -o pipefail

python3.7 -m venv venv
source venv/bin/activate

curl https://bootstrap.pypa.io/pip/3.7/get-pip.py | python -
pip install etl_pipeline --no-index --find-links=$WHEELDIR
