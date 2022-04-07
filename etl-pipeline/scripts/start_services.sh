#!/usr/bin/env bash
set  -e -o pipefail
python ./tests/test_custom/data_source_stub.py &
python -m etl_pipeline