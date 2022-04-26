#!/usr/bin/env bash
set -e -o pipefail

rev=${GIT_COMMIT:-origin/master}
branch=${GIT_BRANCH:-$(git branch --no-color --show-current)}
day=$(git show --no-patch --date=format:'%y%m%d' --pretty=format:'%cd' "$rev")
commit_start=$(git show --no-patch --date=format:'%Y-%m-%d 00:00:00' --pretty=format:'%cd' "$rev")
commit=$(git rev-list --count --since="$commit_start" "$rev")

if [[ "$branch" =~ .*"master" ]]; then
  echo "$day.$commit.0"
else
  echo "HEAD-SNAPSHOT"
fi
