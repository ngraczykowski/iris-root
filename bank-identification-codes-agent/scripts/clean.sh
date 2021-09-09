#!/usr/bin/env bash
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
    "$basedir"/nomad/artifacts/ \
  &>/dev/null || true
