#!/bin/bash
set -xue -o pipefail
# set -o xtrace

# script location is a base dir
CURRENTDIR="$(cd -- "$(dirname -- "${0}")" && pwd -P)"
cd "${CURRENTDIR}"

curl -X PUT "$ES_URL/_opendistro/_security/api/roles/technical_user" \
    -sS \
    -H "Content-Type: application/json" \
    -u "$ES_CREDENTIALS" \
    -d '
  {
    "cluster_permissions": [
      "cluster:admin/opendistro/reports/definition/list",
      "cluster:admin/opendistro/reports/instance/list",
      "cluster:admin/opendistro/reports/instance/get",
      "cluster:admin/opendistro/reports/definition/on_demand",
      "cluster:monitor/health",
      "indices:data/write/bulk",
      "indices:data/read/search",
      "indices:data/read/mget",
      "cluster:admin/opendistro/reports/definition/create",
      "indices:data/read/scroll/clear",
      "indices:admin/template/put"
    ],
    "index_permissions": [
      {
        "index_patterns": [
          "local_production*",
          "local_simulation_*"
        ],
        "fls": [],
        "masked_fields": [],
        "allowed_actions": [
          "indices:data/read/get",
          "indices:data/read/search",
          "indices:data/write/index",
          "indices:data/write/update",
          "indices:data/write/bulk",
          "indices:data/write/bulk*",
          "indices:monitor/settings/get",
          "indices:admin/create",
          "indices:admin/mapping/put",
          "indices:admin/mappings/fields/get",
          "indices:data/write/delete"
        ]
      }
    ],
    "tenant_permissions": [
      {
        "tenant_patterns": [
          "local_*"
        ],
        "allowed_actions": [
          "kibana_all_write"
        ]
      }
    ]
  }
'