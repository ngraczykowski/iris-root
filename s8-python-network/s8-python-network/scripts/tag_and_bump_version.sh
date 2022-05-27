#!/usr/bin/env bash
set -eu -o pipefail

echo 'Tagging and pushing tag to repo'
git tag ${VERSION}
git push --tags origin HEAD:master

echo 'Bumping version - patch for master'
pip install --index-url "${PYPI_URL}" bump2version tox
bump2version patch
git push origin HEAD:master
