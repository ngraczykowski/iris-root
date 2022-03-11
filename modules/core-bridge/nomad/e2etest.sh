#!/usr/bin/env bash
set -e -o pipefail

env=${1}

if [[ -x $(command -v consul) && -x $(command -v jq) ]]; then
  rabbitmq_discovery=$(consul watch -type=service -service=rabbitmq | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')

  core_bridge_db_discovery=$(consul watch -type=service -service=mike-core-bridge-db | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')
  ms_bridge_db_discovery=$(consul watch -type=service -service=mike-ms-bridge-db | jq --raw-output '.[0].Service | [ .Address, (.Port | tostring) ] | join(":")')

  mike_core_bridge_secrets=$(consul kv get mike/core-bridge/secrets)
  mike_ms_bridge_secrets=$(consul kv get mike/ms-bridge/secrets)
fi

test_rabbitmq_vhost="TEST_RABBITMQ_VHOST=/${env}"
test_rabbitmq_addresses="TEST_RABBITMQ_ADDRESSES=${rabbitmq_discovery}"
test_rabbitmq_username="TEST_RABBITMQ_USERNAME=$(echo "$mike_core_bridge_secrets" | grep SPRING_RABBITMQ_USERNAME | cut -d '=' -f 2)"
test_rabbitmq_password="TEST_RABBITMQ_PASSWORD=$(echo "$mike_core_bridge_secrets" | grep SPRING_RABBITMQ_PASSWORD | cut -d '=' -f 2)"

test_core_db_jdbc_url="TEST_CORE_DB_JDBC_URL=jdbc:postgresql://${core_bridge_db_discovery}/${env}-core-bridge"
test_core_db_username="TEST_CORE_DB_USERNAME=$(echo "$mike_core_bridge_secrets" | grep SPRING_DATASOURCE_USERNAME | cut -d '=' -f 2)"
test_core_db_password="TEST_CORE_DB_PASSWORD=$(echo "$mike_core_bridge_secrets" | grep SPRING_DATASOURCE_PASSWORD | cut -d '=' -f 2)"

test_ms_db_jdbc_url="TEST_MS_DB_JDBC_URL=jdbc:postgresql://${ms_bridge_db_discovery}/${env}-ms-bridge"
test_ms_db_username="TEST_MS_DB_USERNAME=$(echo "$mike_ms_bridge_secrets" | grep SPRING_DATASOURCE_USERNAME | cut -d '=' -f 2)"
test_ms_db_password="TEST_MS_DB_PASSWORD=$(echo "$mike_ms_bridge_secrets" | grep SPRING_DATASOURCE_PASSWORD | cut -d '=' -f 2)"

echo "${test_rabbitmq_vhost},${test_rabbitmq_addresses},${test_rabbitmq_username},${test_rabbitmq_password},${test_core_db_jdbc_url},${test_core_db_username},${test_core_db_password},${test_ms_db_jdbc_url},${test_ms_db_username},${test_ms_db_password}"
