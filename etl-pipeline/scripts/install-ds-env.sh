#!/usr/bin/env bash
set -xeu -o pipefail
ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"
export PATH="$ROOT_DIR/env/ds/opt/anaconda/bin:$PATH"

source "$ROOT_DIR"/bash-lib/util.sh
bl_tsecho_err "install-ds-env.sh: starting"

find_latest_file_matching_regex() {
  find "$1" -name "$2" -print0 | xargs -r -0 ls -1 -t | head -1
}

export TMPDIR="$ROOT_DIR/tmp"

DS_INSTALLER_PATH="$(find_latest_file_matching_regex "$ROOT_DIR/env/installer" "ds-installer*.run")"

bl_check_required_variables DS_INSTALLER_PATH

bl_tsecho_err "install-ds-env.sh: Installing $DS_INSTALLER_PATH"
bash "$DS_INSTALLER_PATH" --target "$ROOT_DIR/env/ds"

export POV_PDE_PATH="$ROOT_DIR/env/installer"
echo "$ROOT_DIR"/scripts/install-pip-dependencies-encrypted.sh
echo "$POV_PDE_PATH"

if ! "$ROOT_DIR"/scripts/install-pip-dependencies-encrypted.sh "$POV_PDE_PATH"; then
  bl_tsecho_err "install-ds-env.sh: WARNING! Decryption of pip-dependencies.zip failed. Is install-pip-depedencies-encryted.sh script available?"
else
  bl_tsecho_err "install-ds-env.sh: pip dependencies installed"
fi


bl_tsecho_err "install-ds-env.sh: configuring DS environment"
sed -i "s/^export JUPYTER_WORK_DIR=.*/export JUPYTER_WORK_DIR=\$ROOT_DIR/g" \
  "$ROOT_DIR/env/ds/conf/datascience-settings.sh"

bl_tsecho_err "install-ds-env.sh: completed"
