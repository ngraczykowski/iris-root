#!/usr/bin/env bash
set  -e -o pipefail
python -m etl_pipeline --ssl &
python ./tests/test_json/data_source_stub.py --ssl