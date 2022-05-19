#!/bin/bash

set -eu -o pipefail

/usr/bin/python3.6 -m venv venv
source venv/bin/activate
PYTHON_VERSION=`python --version`

echo $PYTHON_VERSION
ls -l local/app/temp
curl https://bootstrap.pypa.io/pip/3.6/get-pip.py | python -
pip install --no-index --no-deps local/app/temp/*.whl

echo Service is installed
echo Service is starting
export PYTHONIOENCODING=UTF-8
export CONSUL_SECRET_PATH=mike/etl_service/secrets
python -m etl_pipeline