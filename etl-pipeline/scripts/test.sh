#!/usr/bin/env bash
set -eu -o pipefail

docker-compose -f docker/test/docker-compose-test.yaml run test_jupyter_playground