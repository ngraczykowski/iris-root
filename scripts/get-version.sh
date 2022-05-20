#!/usr/bin/env bash
set -e -o pipefail

rev=${GIT_COMMIT:-origin/master}
day=$(git show --no-patch --date=format:'%y%m%d' --pretty=format:'%cd' "$rev")
commit_start=$(git show --no-patch --date=format:'%Y-%m-%d 00:00:00' --pretty=format:'%cd' "$rev")
commit=$(git rev-list --count --since="$commit_start" "$rev")

echo "$day.$commit.0"
