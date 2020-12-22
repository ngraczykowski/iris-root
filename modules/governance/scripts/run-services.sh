#!/usr/bin/env bash
set -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

cd "$scriptdir"/..
docker-compose --compatibility up -d
