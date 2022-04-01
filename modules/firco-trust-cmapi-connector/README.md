# Firco Trust CMAPI Connector (FTCC)

## Documentation

Whimsical [HLD](https://whimsical.com/bridge-x5mk356DKDpj15vBzH4zn) diagram.

## Modules

- [ftcc-app](./ftcc-app) - entrypoint module,
- [ftcc-callback](./ftcc-callback) - callback module - handle `BatchCompleted` messages, request for `GetRecommendation` and generate [`ClientRequestDto`](ftcc-common/src/main/java/com/silenteight/connector/ftcc/common/dto/output/ClientRequestDto.java) for callback, 
- [ftcc-common](./ftcc-common) - common classes,
- [ftcc-common-testing](./ftcc-common-testing) - common testing classes,
- [ftcc-db-changelog](./ftcc-db-changelog) - changelog for liquibase,
- [ftcc-ingest](./ftcc-ingest) - ingest module - accepting incoming requests, register batch.

## Spring Profiles:
- **mockcallback** - expose `/callback` endpoint to handle `ClientRequestDto`,
- **mockcorebridge** - mock `core-bridge` service,

## Development

- Java: **11**,
- Spring Boot: **2.6.6**

