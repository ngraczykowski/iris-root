#!/usr/bin/env bash
set -xeu -o pipefail
ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"

source "$ROOT_DIR"/bash-lib/util.sh
bl_tsecho_err "install-spark-ivy.sh: starting"

find_latest_file_matching_regex() {
  find "$1" -name "$2" -print0 | xargs -r -0 ls -1 -t | head -1
}

SPARK_IVY_PATH="$(find_latest_file_matching_regex "$ROOT_DIR/env/installer" "spark-ivy*.tar.bz2")"

bl_check_required_variables SPARK_IVY_PATH

bl_tsecho_err "install-spark-ivy.sh: Installing $SPARK_IVY_PATH"

mkdir -p $HOME/.ivy2 && tar xf "$SPARK_IVY_PATH" --strip-component=1 -C $_

bl_tsecho_err "install-spark-ivy.sh: completed"
