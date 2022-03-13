#!/usr/bin/env bash
set  -e -o pipefail
kill $(ps aux | grep '[p]ython -m etl_pipeline' | awk '{print $2}')
kill $(ps aux | grep '[p]ython ./tests/test_custom/data_source_stub.py' | awk '{print $2}')