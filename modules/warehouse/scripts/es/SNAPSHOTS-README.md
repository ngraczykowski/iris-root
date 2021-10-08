[TOC levels=2-4 numbered]: # "Snapshots"

# Governance
1. [Prerequisites](#prerequisites)
2. [Creating policy](#creating-policy )
3. [Applying policy](#applying-policy )
4. [Manually creating and restoring snapshot](#manually-creating-and-restoring-snapshot)
   1. [Creating snapshot ](#creating-snapshot)
   2. [Restoring snapshots](#restoring-snapshots)
   3. [Reindexing](#reindexing)
   4. [Removing-restored-index](#removing-restored-index)
5. [Troubleshooting](#troubleshooting)

## Prerequisites
 - network connection with Minio server
 - ElasticSearch plugin: repository-s3 installed
 - bucket for snapshots in minio created.

> **NOTE**
>
> Ensure that user defined for repository s3 has read/write access to bucket

## Create S3 repository in minio
```
PUT _snapshot/repository_daily 
{
    "type": "s3",
    "settings": {
        "endpoint": "minio:9000",
        "protocol": "http",
        "bucket": "snapshots",
        "base_path": "daily",
        "path_style_access": true
    }
}
```
for more details visit: https://opendistro.github.io/for-elasticsearch-docs/docs/elasticsearch/snapshot-restore/#amazon-s3

## Creating policy 
```
PUT _opendistro/_ism/policies/daily_snapshot
{
  "policy": {
    "description": "A policy to create daily snapshots.",
    "default_state": "hot",
    "states": [
        {
            "name": "hot",
            "actions": [],
            "transitions": [
                {
                    "state_name": "cold",
                    "conditions": {
                        "cron": {
                            "cron": {
                                "expression": "1 30 * * *",
                                "timezone": "Europe/Warsaw"
                            }
                        }
                    }
                }
            ]
        },
        {
            "name": "cold",
            "actions": [
                {
                    "snapshot": {
                        "repository": "repository_daily",
                        "snapshot": "snapshot_daily"
                    }
                }
            ],
            "transitions": [
                {
                    "state_name": "hot"
                }
            ]
        }
    ],
    "ism_template": null
  }
}
```
## Applying policy
```
POST _opendistro/_ism/add/local_production
{
  "policy_id": "daily_snapshot"
}
```

## Checking policy state
```
GET _opendistro/_ism/explain/local_production
```

for more details visit: https://opendistro.github.io/for-elasticsearch-docs/docs/im/ism/



## Manually creating and restoring snapshot
#### Creating snapshot
```
PUT _snapshot/repository_daily
{
  "type": "s3",
  "settings": {
      "endpoint": "minio:9000",
      "protocol": "http",
      "bucket": "snapshots",
      "base_path": "daily",
      "path_style_access": true
  }
}
```
#### Restoring snapshots
```
POST _snapshot/repository_daily/snapshot_daily-2021.09.28-11:37:37.458/_restore
{
    "indices": "local_production",
    "ignore_unavailable": true,
    "include_global_state": false,
    "include_aliases": false,
    "partial": false,
    "index_settings": {
        "index.blocks.read_only": false
    },
    "rename_pattern": "local_production",
    "rename_replacement": "restored_local_production",
    "ignore_index_settings": [
        "index.refresh_interval"
    ]
}
```

#### Reindexing
Reindex API operation to rename "restored_local" back to "local"
```
POST _reindex
{
    "source": {
        "index": "restored_local_production"
    },
    "dest": {
        "index": "local_production"
    }
}
```
#### Removing-restored-index
Remove restored index
```
    DELETE /restored_local_production
```

## Troubleshooting

####ensure s3-plugin is making a proper request to minio server 
```
./mc admin trace -a -v minio
```
####ensure user defined in s3-plugin has proper access to bucket
```
./mc policy list minio/snapshots
```
for more information visit: https://nm-muzi.com/docs/minio-client-complete-guide.html