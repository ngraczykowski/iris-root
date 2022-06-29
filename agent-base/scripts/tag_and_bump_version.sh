#!/usr/bin/env bash
set -eu -o pipefail

branch="${CI_COMMIT_REF_NAME:-}"
if [[ -z "${branch}" ]]; then
  branch=$(git rev-parse --abbrev-ref HEAD)
fi

echo 'Tagging and pushing tag to repo'
git tag ${VERSION}
git push --tags origin HEAD:"${branch}"

echo 'Bumping version - patch for master'
pip install --index-url "${PYPI_URL}" bump2version
bump2version patch
git push origin HEAD:"${branch}"
