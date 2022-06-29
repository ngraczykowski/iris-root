#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"

rm -rf \
    "$basedir"/build/ \
    "$basedir"/dist/ \
    "$basedir"/.pytest_cache/ \
    "$basedir"/.eggs \
    "$basedir"/.tox \
    "$basedir"/*.egg-info \
    "$basedir"/.coverage \
    "$basedir"/nomad/artifacts \
    "$basedir"/nomad/*.tar.bz2 \
  &>/dev/null || true
