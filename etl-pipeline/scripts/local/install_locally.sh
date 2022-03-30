#!/usr/bin/env bash
set -eu -o pipefail

python3.6 -m venv venv
source venv/bin/activate

curl https://bootstrap.pypa.io/pip/3.6/get-pip.py | python -
pip install etl_pipeline --no-index --find-links=$WHEELDIR
`   `