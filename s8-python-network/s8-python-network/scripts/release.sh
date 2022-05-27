#!/usr/bin/env bash
set -eu -o pipefail

function gitRelease() {
  echo 'Releasing'
  bump2version minor
  echo 'Building'
  scripts/clean.sh
  scripts/build.sh "$@"
}

branch="${CI_COMMIT_REF_NAME:-}"
if [[ -z "${branch}" ]]; then
  branch=$(git rev-parse --abbrev-ref HEAD)
fi

if [ "${branch}" == "master" ]; then
  echo 'Releasing - bumping minor'
  gitRelease "$@"
else
  echo 'Not a release branch'
  exit 0
fi

echo 'Pushing'

git push --atomic --tags origin HEAD:"${branch}"
