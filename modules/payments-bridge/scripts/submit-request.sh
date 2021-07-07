#!/usr/bin/env bash

curl -v \
    -H 'Content-Type: application/json' \
    -d '{"alerts": [{"systemId": "01234"}]}' \
    http://localhost:8080/foo?dc=UK