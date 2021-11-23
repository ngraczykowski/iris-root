#!/usr/bin/env bash
set  -e -o pipefail
scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

IFS='-' read -r name version rest <<< $(basename -- "$(ls -tr ./dist/*.whl)")

artifact=$(basename -- "$(ls -tr ./dist/*.pyz)")
artifact_path="$(ls "./dist/$artifact")"
config="${name}-config-${version}.tgz"
config_path="$(ls "./dist/${config}")"

set -x

rm -rf ./nomad/artifacts/
mkdir -p ./nomad/artifacts/

cp "$artifact_path" ./nomad/artifacts/
cp "$config_path" ./nomad/artifacts/

cd nomad
tar -cvjSf ../dist/"${name}-nomad-${version}.tar.bz2" -- *
cd -