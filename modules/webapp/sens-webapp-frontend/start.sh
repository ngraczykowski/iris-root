#!/usr/bin/env bash
set -e

basedir="$(cd -- "$(dirname -- "${0}")" && pwd)"

export SENS_HOME="$(cd -- "$basedir"/../ && pwd)"

if [ -z "$SENS_FRONTEND_SKIP_BUILD" ]; then
    echo "=== BUILDING SENS-FRONTEND ==="

    "$basedir"/../scripts/module-build.sh \
        :sens-frontend \
        clean verify \
        -Dmaven.test.skip=true
fi

echo "=== STARTING SENS-FRONTEND ==="

cd "$basedir"
./npm start "$@"
