#!/usr/bin/env bash
set -eu -o pipefail

function gitRelease() {
  echo 'Releasing'
  bump2version minor
  echo 'Building'
  scripts/clean.sh
  scripts/build.sh "$@"
#  echo 'Resetting release commit'
#  git reset --hard HEAD~1 # remove release commit after tag
}

branch="${CI_COMMIT_REF_NAME:-}"
if [[ -z "${branch}" ]]; then
  branch=$(git rev-parse --abbrev-ref HEAD)
fi

if [ "${branch}" == "master" ]; then
  echo 'Releasing - bumping minor'
  gitRelease "$@"
  echo 'Bumping patch'
  bump2version patch # for master
else
  echo 'Not a release branch'
  exit 0
fi

echo 'Pushing'

git push --atomic --tags origin HEAD:"${branch}"
