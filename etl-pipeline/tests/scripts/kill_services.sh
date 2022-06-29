#!/usr/bin/env bash

kill -9 $(ps aux | grep '[p]ython -m etl_pipeline' | awk '{print $2}')
kill -9 $(ps aux | grep 'multiprocessing' | awk '{print $2}')
kill -9 $(ps aux | grep 'tests/test_json/data_source_stub.py' | awk '{print $2}')
kill -9 $(ps aux | grep 'tests/test_json/data_source_stub.py --ssl' | awk '{print $2}')
