# Scb Bridge

SCB Bridge application uses **Java 17**

# Local run

To start an app locally:

- run `docker-compose up -d`
- start class `ScbBridgeApplication` with profile `client`

## Application ports

| Service  | Port  |
|:---------|:------|
| HTTP     | 24220 |
| GRPC     | 24221 |

## Accessing services in Docker

Services are exposed on locally accessible port numbers. The table below shows how to access them.

| Service  | URL                       | User   | Password |
|:---------|:--------------------------|:-------|:---------|
| RabbitMQ | http://localhost:5681/    | `serp` | `serp`   |
| Consul   | http://localhost:8500/ui/ |        |          |

## Externalizing configuration

Configuration (yaml file) can be placed in Consul under key `config/scb-bridge/data`.

This configuration scheme:

- will be the primary way of configuring `scb-bridge` with the business related settings like Oracle
  location, Jobs settings, etc.
- will be changed/tweaked by scb
- after a config change in Consul, `scb-bridge` needs to be restarted in order the new config to be
  applied

### Local run

To enable locally getting configuration from Consul activate profile `consul`. You would want to
have Consul running locally as well, you may start it with the help of attached `docker-compose`
file.

If you don't want to use Consul, start the application with `client` profile, so it has some sane
values how to behave locally.

Keep in mind that `consul` profile will be activated on client's environment, and our test envs as
well.

## How to test

Use following urls to access testing endpoints on specific environments:

| Environment | Url                             |
|:------------|:--------------------------------|
| Local       | `http://localhost:24220/`       |
| `lima`      | `https://lima.silenteight.com/` |

### CBS

#### Simulating invoking Queueing Job

POST json

```json
{
  "totalRecordsToRead": 1
}
```

to `/rest/scb-bridge/v1/cbs/test/invokeQueueingJob` endpoint. You can specify more options,
otherwise following defaults will be used

```json
{
  "alertIdContext": {
    "ackRecords": true,
    "hitDetailsView": "",
    "priority": 10,
    "watchlistLevel": true,
    "recordsView": "SENS_V_FFF_RECORDS_DENY"
  },
  "chunkSize": 1000
}

```

#### Simulating processing of single alert

POST json

```json
{
  "systemId": "SYSTEM_ID"
}
```

to `/rest/scb-bridge/v1/cbs/test/queueAlert` endpoint. You can specify more options, otherwise
following defaults will be used

```json
{
  "alertIdContext": {
    "ackRecords": true,
    "hitDetailsView": "",
    "priority": 10,
    "watchlistLevel": true,
    "recordsView": "SENS_V_FFF_RECORDS_DENY"
  }
}
```

### GNS-RT

The simplest way to test GNS-RT locally is to send a JSON request using Postman:

1. Generate new request using
   endpoint `http://localhost:24220/rest/scb-bridge/v1/gnsrt/system-id/random` (
   method `GET`)
    1. It pulls one random record from Oracle DB and based on its data it generates a JSON request.
    2. In addition, it modifies system_id, so there won't be any duplicates in scb_raw_alert table.
2. Copy generated request and send it to
   endpoint `http://localhost:24220/rest/scb-bridge/v1/gnsrt/recommendation` (
   method `POST`)
    1. To paste the request go to `Body` section
    2. Select `raw`
    3. Change type to `JSON`

#### Nomad

You can also use Postman to send a request to SCB-Bridge that is deployed on `Nomad`. What you need
to do is to:

- Have access to Nomad by VPN
- Change url in request to `https://lima.silenteight.com/rest/scb-bridge/` + endpoint name eg:
    - `https://lima.silenteight.com/rest/scb-bridge/v1/gnsrt/recommendation`
    - `https://lima.silenteight.com/rest/scb-bridge/v1/gnsrt/system-id/random`

## Test script

For your convenience you may use [testing script](tests/test-random-requests.sh) which issues gns-rt 
random requests and logs the responses.

## Running tests in Gradle

By default, build run all tests. However, some integration tests take too much time. In order to
make work faster, we added the flag `-Punit` to run only unit tests. <br>

Example <br>
`./gradlew clean build ` - run build with all tests <br>
`./gradlew clean build -PunitTests` - run build only with unit tests

This same works with command `test` <br>
`./gradlew clean test ` - run all tests <br>
`./gradlew clean test -PunitTests` - run only unit tests

Tests are considered as integration when they have the word `IT` as postfix in the class name
eg: `DatabaseIntegrationTestIT` or they have the word `Integration` in the class name
eg: `DatabaseIntegrationTest`. So it's important to name test classes appropriately.

## Nomad Deployment

The Nomad deployment descriptor(the job file) `scb-bridge.nomad` contains job specification and all
its requirements. Nomad scheduler deployed on-premise will use this file to run scb-bridge
artifact (jar).

## Providing learning alert data by ECM (Hive)

1) Login via ssh to the hive server
2) cd to bin
3) Execute these commands:

* export EEL_TMP_DIR=/tmp
* export EEL_PG_HOST=10.23.234.xx
* export EEL_PG_PORT=6524
* export EEL_PG_DBNAME=tsaas-1.40.0-BUILD.703
* export EEL_PG_USERNAME=devadmin
* export EEL_PG_PASS=Devadmin@123

and then, run the script: bridge-dist/src/bash/export_ecm_learning.sh

## QCO module

The QCO (Quality Control Operations) is a process where the bridge delivers recommendations to
banking ECM system but with overloaded solution according to provided rules (configuration *.csv
file). The configuration allows defining rule(s) to determine which the policy and step of
alert/match should change solution for. It also defines the frequency (threshold) for given step
which is the size of the alert/matches distribution for candidate selection. In order to trigger QCO
sampling and solution overriding:

The configuration file has to be provided at the location defined
in `bridge-qco/src/main/resources/application.yml`.

The configuration allows defining rule to determine which the policy and step of alert we should
change solution for. The configuration allows defining how frequent we should get alert to analyze
as well.

### How to enable QCO

The QCO process can be enabled by setting property
`silenteight.qco.enabled` to `true`

## EXTENDED VERSION OF RECOM FUNCTION WITH QCO PARAMETERS

To enable recom function with qco parameters, we need to set property:

```
silenteight:
  scb-bridge:
    cbs:
      attach-qco-fields-to-recom: true
```

Function signature:

    FUNCTION F_CBS_S8_LOG_RECOM
    (
        P_SOURCE_APPLN                  IN VARCHAR2, 
        P_SYSTEM_ID                     IN VARCHAR2, 
        P_BATCH_ID                      IN VARCHAR2, 
        P_HIT_WATCHLIST_ID              IN VARCHAR2, 
        P_HIT_RECOMMENDED_STATUS        IN VARCHAR2, 
        P_HIT_RECOMMENDED_COMMENTS      IN CLOB,
        P_LIST_RECOMMENDED_STATUS       IN VARCHAR2, 
        P_LIST_RECOMMENDED_COMMENTS     IN CLOB,
        P_POLICY_ID                     IN VARCHAR2, 
        P_HIT_ID                        IN VARCHAR2, 
        P_STEP_ID                       IN VARCHAR2, 
        P_FV_SIGNATURE                  IN VARCHAR2
        P_QA_SAMPLED                    IN VARCHAR2
    )
    RETURN VARCHAR2 AS PRAGMA AUTONOMOUS_TRANSACTION;