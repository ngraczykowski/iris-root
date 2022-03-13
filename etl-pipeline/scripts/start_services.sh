#!/usr/bin/env bash
set  -e -o pipefail
python -m etl_pipeline &
python ./tests/test_custom/data_source_stub.py