#!/usr/bin/env bash
set -x -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"

find "$scriptdir"/.. -type d \( -name '.gradle' -o -name 'build' \) -exec rm -rf {} +
