#!/usr/bin/env bash
set  -e -o pipefail
kill -9 $(ps aux | grep '[p]ython -m etl_pipeline' | awk '{print $2}')
kill -9 $(ps aux | grep 'tests' | awk '{print $2}')