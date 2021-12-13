#!/usr/bin/env bash
set -eu -o pipefail
DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"
ANACONDA_INSTALL_DIR="$DIR/anaconda"

ANACONDA_FILE_NAME=$(basename -- "$(ls "$DIR/artifacts/anaconda/")")

rm -rf "$ANACONDA_INSTALL_DIR"
echo "Installing anaconda in $ANACONDA_INSTALL_DIR"
"$DIR/artifacts/anaconda/$ANACONDA_FILE_NAME" -b -p "$ANACONDA_INSTALL_DIR"

rm -rf "$ANACONDA_INSTALL_DIR"/.cph_tmp*
