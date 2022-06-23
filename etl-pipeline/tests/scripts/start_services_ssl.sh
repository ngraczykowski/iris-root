#!/usr/bin/env bash
set  -e -o pipefail
python -m etl_pipeline --ssl --disable_learning &
python ./tests/test_json/data_source_stub.py --ssl