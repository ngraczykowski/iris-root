# Scb Bridge

SCB Bridge application uses **Java 17** 

## Application ports

| Service  | Port  |
|:---------|:------|
| HTTP     | 24220 |
| GRPC     | 24221 |

## Accessing services in Docker

Services are exposed on locally accessible port numbers. The table below shows how to access them.

| Service    | URL                      | User    | Password   |
|:-----------|:-------------------------|:--------|:-----------|
| RabbitMQ   | http://localhost:5681/   | `serp`  | `serp`     |

## How to test

### GNS-RT

The simplest way to test GNS-RT locally is to send a JSON request using Postman:
1. Generate new request using endpoint `http://localhost:24220/v1/gnsrt/system-id/random` (method `GET`)
   1. It pulls one random record from Oracle DB and based on its data it generates a JSON request.
   2. In addition, it modifies system_id, so there won't be any duplicates in scb_raw_alert table. 
2. Copy generated request and send it to endpoint `http://localhost:24220/v1/gnsrt/recommendation` (method `POST`)
   1. To paste the request go to `Body` section
   2. Select `raw`
   3. Change type to `JSON`

#### Nomad

You can also use Postman to send a request to SCB-Bridge that is deployed on `Nomad`.
What you need to do is to:
- Have access to Nomad by VPN
- Change url in request to `http://10.8.0.2:<SCB_BRIDGE_PORT>/v1/gnsrt/recommendation`
- `<SCB_BRIDGE_PORT>` after each restart SCB-Bridge can get a random port - it means you have to first check on Nomad what's the current Bridge port.

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

The Nomad deployment descriptor(the job file) `scb-bridge.nomad` contains job specification and all its requirements. 
Nomad scheduler deployed on-premise will use this file to run scb-bridge artifact (jar).

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