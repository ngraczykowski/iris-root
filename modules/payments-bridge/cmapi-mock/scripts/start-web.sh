#!/usr/bin/env bash

DIR="$(cd -- "$(dirname -- "$0")" && pwd)"

source "$DIR/../venv/bin/activate"

export FLASK_APP="$DIR/../app/app"
export FLASK_ENV=development
export MOCKAPI_SETTINGS="$DIR/../settings.cfg"

flask run "$@"
