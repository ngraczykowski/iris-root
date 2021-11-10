#!/usr/bin/env bash
set -eU -o pipefail

function gitRelease() {
  echo 'Releasing'
  bump2version release
  echo 'Building'
  scripts/clean.sh
  scripts/build.sh
  echo 'Resetting release commit'
  git reset --hard HEAD~1 # remove release commit after tag
}

branch="${CI_COMMIT_REF_NAME}"
if [[ -z "${branch}" ]]; then
  branch=$(git rev-parse --abbrev-ref HEAD)
fi

if [ "${branch}" == "master" ]; then
  echo 'Releasing master'
  gitRelease
  echo 'Bumping minor'
  bump2version minor # for master
elif [[ $branch == release* ]]; then
  echo 'Releasing branch'
  gitRelease
  echo 'Bumping patch'
  bump2version patch # for release
else
  echo 'Not a release branch'
  exit 0
fi

echo 'Pushing'

git push --atomic --tags origin HEAD:$branch
