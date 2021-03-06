# Silent Eight Warehouse

## Configuration management

There are multiple yml configuration files in the `warehouse-app/src/main/resources/config`:

- `application.yml` contains all application-related configuration
- `application-dev.yml` contains configuration convenient for development
such as logging, pretty-print, etc.
- `application-swagger.yml` enables and configures swagger
- `application-tls.yml` enables and configures tls

Additionally, there are two more `application.yml`:
- `${projectDir}/config/application.yml` - contains all deployment-related configuration required
  to run the application locally. This file can be considered to be a base for creating *.yml configuration
  for any other deployments, e.g. nomad deployment stored in `nomad/conf/application.yml`  
- `nomad/conf/application.yml` - a template rendered by Nomad that results in a deployment
  configuration that targets hosted environments.

## Development Setup

Before you can run Warehouse, you need to have a few infrastructural services running:

1. RabbitMQ - the message broker for communicating between components and with outside services.
2. MinIO - a S3 compliant reportStorage service
3. PostgreSQL - SQL database
4. Keycloak - IAM

To start all services see: [Iris - Running Application(s) locally](../../README.md#Running-Application(s)-locally)

## Testing

1.  Deliver some test data to the application.
    For the local testing purpose you can start via Intellij/RunConfiguration:

    -   `WarehouseTestApplication (Sim)` to generate random data for simulation flow.
        The analysisId is randomly generated at application startup.

    -   `WarehouseTestApplication (Prod)` to generate random data for production flow.

### Swagger-ui

In order to run the application with swagger enabled apply 'swagger' Spring profile.

Swagger UI is accessible via [http](http://localhost:24900/rest/warehouse/openapi/swagger-ui/index.html?configUrl=/rest/warehouse/openapi/api-docs/swagger-config).

To authenticate with keycloak you should enter:

        clientId: frontend
        clientSecret: <empty>

Then you need to login as frontend user

### Keycloak configuration

Keycloak configuration resides in docker-compose/keycloak/imports/Dev-realm.json

FYI: when something changes in dev-realm.json (from git pull for example) we need to docker-compose down -v to recreate docker volume because keycloak is caching devrealm.json file

## Other

### Running integration test against local environment

1. Remove initialization of TestContainers by commenting/ removing ContextConfiguration initializers:

        @ContextConfiguration(initializers = {
        //    PostgresTestInitializer.class,
        })

2. Activate local profile which provides connection setup for local environment.
   You may want to enable debug profile.

        @ActiveProfiles({ "local", "debug" })

### Setting-up HTTPS for dev environment

#### Starting minio in TLS
If you want to run services with TLS enabled you need to use [docker-compose.https](docker-compose.https.yml):

    docker-compose --compatibility -f docker-compose.yml -f modules/warehouse/scripts/docker-compose.https.yml up -d

#### Generating keys for minio

        cd scripts/minio
        openssl genrsa -out private.key 2048
        openssl req -new -x509 -nodes -days 730 -key private.key -out public.crt -config openssl.conf

#### Truststore
There is a test truststore in `./scripts`. You can re-create it by following the steps below:

        cd scripts
        keytool -import -file minio/public.crt -alias minioCA -keystore myTrustStore

The path to truststore and password needs to be provided via environment variables as explained below.

#### SSL setup
Set the following environment variables:

        TRUSTSTORE_PATH=scripts/myTrustStore     # use ralative or absolute path
        TRUSTSTORE_PASSWORD=password

Note: The application binds these environment variables to system properties.
Setting these via `application.yml` has no effect.
