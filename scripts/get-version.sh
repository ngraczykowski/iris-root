#!/usr/bin/env bash
set -e -o pipefail

rev=${GIT_COMMIT:-origin/master}
day=$(git show --no-patch --date=format:'%y%m%d' --pretty=format:'%cd' "$rev")
last_commit_timestamp=$(git show --no-patch --date=format:'%Y-%m-%d %H:%M:%S' --pretty=format:'%cd' "$rev")
commit=$(git rev-list --count origin/master --since="$last_commit_timestamp")
build=${BUILD_NUMBER:-0}
sha=$(git rev-parse --short "$rev")

echo "$day.$commit.$build+$sha"
