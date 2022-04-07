#!/usr/bin/env bash
set  -e -o pipefail
kill -9 $(ps aux | grep '[p]ython -m etl_pipeline' | awk '{print $2}')
kill -9 $(ps aux | grep 'tests/test_custom/data_source_stub.py' | awk '{print $2}')