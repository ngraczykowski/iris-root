#!/usr/bin/env bash
set  -e -o pipefail

python ./tests/test_json/data_source_stub.py $2 &
python -m etl_pipeline "$@"