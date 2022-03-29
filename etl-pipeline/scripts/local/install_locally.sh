#!/usr/bin/env bash
set -eu -o pipefail

python3.6 -m venv venv
source venv/bin/activate

pip install etl_pipeline --no-index --find-links=$WHEELDIR
