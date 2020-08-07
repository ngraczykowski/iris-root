#!/usr/bin/env bash
set -e

function gitRelease() {
  bump2version release
  scripts/clean.sh
  scripts/build.sh
  git reset --hard HEAD~1 # remove release commit after tag
}

branch="${CI_COMMIT_REF_NAME}"
if [ -z "${branch}" ]; then
  branch=$(git rev-parse --abbrev-ref HEAD)
fi

if [ "${branch}" == "master" ]; then
  gitRelease
  bump2version minor # for master
elif [[ $branch == release* ]]; then
  gitRelease
  bump2version patch # for release
else
  echo 'Not a release branch'
  exit 0
fi

git push --atomic --tags origin HEAD:$branch

