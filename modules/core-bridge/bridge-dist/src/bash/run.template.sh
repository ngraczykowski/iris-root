#!/usr/bin/env bash
set -e -o pipefail
bindir="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
basedir="$(cd -- "$bindir"/.. && pwd -P)"

JAVA_OPTS=(
    -Dfile.encoding=UTF-8
    -Dsun.jnu.encoding=UTF-8
    -Djava.security.egd=file:/dev/urandom
)

# Parse all arguments before -- as java options, and all remaining as jar arguments.
# If there is no --, treat all arguments as jar arguments
ARGS=()
while [[ $# -gt 0 ]]; do
    if [[ $1 == "--" ]]; then
        JAVA_OPTS+=( "${ARGS[@]}" )
        shift
        break
    fi

    ARGS+=( "$1" )
    shift
done

if [[ $# -eq 0 ]]; then
    set -- "${ARGS[@]}"
fi

exec java "${JAVA_OPTS[@]}" -jar "$basedir"/lib/@@jarfile@@ "$@"
