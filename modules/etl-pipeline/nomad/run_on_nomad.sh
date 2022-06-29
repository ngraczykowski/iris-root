#!/bin/bash

set -eu -o pipefail

python3.7 -m venv venv
source venv/bin/activate
PYTHON_VERSION=`python --version`

echo $PYTHON_VERSION
pip install wheel setuptools pip
ls -l /app
pip install --no-index --no-deps /app/app/temp/*.whl

echo Service is installed
echo Service is starting
export PYTHONIOENCODING=UTF-8
export CONSUL_SECRET_PATH=mike/etl_service/secrets
python -m etl_pipeline
