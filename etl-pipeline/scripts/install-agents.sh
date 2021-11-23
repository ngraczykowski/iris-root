#!/usr/bin/env bash
# set -xeu -o pipefail
ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"
export PATH="$ROOT_DIR/env/ds/opt/anaconda/bin:$PATH"

source "$ROOT_DIR"/bash-lib/util.sh
bl_tsecho_err "install-agents.sh: starting"

AGENTS_DIR="$ROOT_DIR/env/agents"

mkdir -p "$AGENTS_DIR"

for f in "$ROOT_DIR"/env/installer/*agent*.tar.bz2
do

  basename="$(basename -- ${f%.tar.bz2})"
  dirname=${AGENTS_DIR}/${basename}
  if [[ ! -d $dirname ]]; then
    tar xjvf $f -C $AGENTS_DIR
  else
    echo "Agent directory ${dirname} already exists"
  fi
done 

bl_tsecho_err "install-agents.sh: completed"
