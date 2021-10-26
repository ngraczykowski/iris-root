#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -z $TWINE_REPOSITORY_URL ]]; then
  echo "ERROR: TWINE_REPOSITORY_URL environmental variable not set!"
  exit 1
fi

twine upload --non-interactive dist/*.whl
