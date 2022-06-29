#!/usr/bin/env bash
set -eu -o pipefail

scriptdir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$scriptdir"/.. && pwd -P)"
cd "$basedir"

if [[ -d venv ]]; then
  source venv/bin/activate
fi

rm -rf etl-pipeline-dist
mkdir -p dist etl-pipeline-dist/lib
pip wheel . -w etl-pipeline-dist/lib --no-cache-dir
pip download -d etl-pipeline-dist/lib pip
artifact=$(basename -- "$(ls ./etl-pipeline-dist/lib/etl_pipeline-*.whl)")
version=$(ls -al "./etl-pipeline-dist/lib/$artifact" | awk -F'etl_pipeline-|-py3-none-any.whl' '{print $2}')
mv etl-pipeline-dist etl-pipeline-dist-$version
cp -R config etl-pipeline-dist-$version
tar cjf dist/etl-pipeline-dist-$version.tar.bz2 etl-pipeline-dist-$version
rm -rf etl-pipeline-dist-$version
