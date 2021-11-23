#!/usr/bin/env bash
set -xeu -o pipefail

ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"

echoerr() {
    echo "$@" >&2
}


ENCRYPTION_PASSWORD="hkcVJgdybKvhRDnqCLK6Bgw"

PDE_TARGET_DIR="$ROOT_DIR/env/installer"
PDE_FILENAME="pip-dependencies-encrypted.zip"
PDE_TARGET_PATH="$PDE_TARGET_DIR"/$PDE_FILENAME
echo $PDE_TARGET_PATH

if [[ ! -f "$PDE_TARGET_PATH" ]]; then

    if [[ "$#" -ne 1 ]]; then
        echoerr "You need to provide path to pip-depedencies-encrypted.zip file"
        exit 1
    else 
        PDE_TARGET_PATH="$1"
    fi 
fi

cd "$PDE_TARGET_DIR"
unzip -P "$ENCRYPTION_PASSWORD" "$PDE_FILENAME"

if [[ -d "pip-dependencies" ]]; then
    mv "pip-dependencies" "pip-dependencies-$(date +%Y%m%d-%H%M%S).bkp"
fi

unzip pip-dependencies.zip

"$ROOT_DIR"/scripts/install-pip-dependencies.sh
