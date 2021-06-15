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

## Tenants configuration

Warehouse depends heavily on the concept of tenants introduced in Opendistro and uses it
to separate distinct reports.

The following summarizes the tenants that should be preconfigured in the environment:

- `[env]_production_ai_reasoning` - production flow, 'ai-reasoning' report definitions
- `[env]_production_rb_scorer` - production flow, 'rb-scorer' report definitions
- `[env]_production_accuracy` - production flow, 'accuracy' report definitions
- `[env]_production_periodic` - production flow, reports that should be generated periodically and stored in Minio for auditing 
- `[env]_simulation_master` - simulation flow, reports definitions blueprint

These are the tenants create by application: 
- `[env]_simulation_[analysisId]` - simulation flow, tenant created based on `[env]_simulation_master`.
   Contains all the simulation reports related to a specific `analysisId`.

RFU:
- `[env]_production_custom` - RFU, production flow, custom report definitions created by user
- `[env]_simulation_custom` - RFU, simulation flow, custom report definitions created by user

## Development Setup

Before you can run Warehouse, you need to have a few infrastructural services running:

1. RabbitMQ - the message broker for communicating between components and with outside services.
2. ElasticSearch - NoSQL database optimized for searches
3. Kibana - an open source reporting tool
4. MinIO - a S3 compliant storage service
5. PostgreSQL - SQL database

### Starting RabbitMQ 
To start RabbitMQ, follow the steps:

1. Clone [Common Docker Infrastructure]:

       git clone https://gitlab.silenteight.com/sens/common-docker-infrastructure
       
1. Start docker-compose. It is sufficient to start just the rabbitmq service.
    
       docker-compose up -d rabbitmq

### Starting ElasticSearch, Kibana, MinIO and PostgreSQL
To start the services run:

        ./scripts/run-services.sh
        
If you need to run services with TLS enabled:

        ./scripts/run-service-https.sh

### Initializing ElasticSearch and Kibana
If you wish to play with ElasticSearch and Kibana you need to populate these with some data first.
You may want to use the following convenience script that:

        ./scripts/init.sh

or in the case of https setup:

        ./scripts/init-https.sh

## Testing

### Swagger-ui

In order to run the application with swagger enabled apply 'swagger' Spring profile.

Swagger UI is accessible via [http](http://localhost:24900/rest/warehouse/openapi/swagger-ui/index.html?configUrl=/rest/warehouse/openapi/api-docs/swagger-config).

## Other

### Running integration test against local environment

1. start infrastructure:
    
        ./scripts/run-services.sh
    
2. Remove initialization of TestContainers by commenting/ removing ContextConfiguration initializers:

        @ContextConfiguration(initializers = {
        //    OpendistroElasticContainerInitializer.class,
        //    OpendistroKibanaContainerInitializer.class
        })
        
3. Activate local profile which provides connection setup for local environment.
   You may want to enable debug profile.

        @ActiveProfiles({ "local", "debug" })

### Setting-up HTTPS for dev environment 

#### Generating keys for minio

        cd scripts/minio
        openssl genrsa -out private.key 2048
        openssl req -new -x509 -nodes -days 730 -key private.key -out public.crt -config openssl.conf

#### Truststore
There is a test truststore in `./scripts`. You can re-create it by following the steps below:

        cd scripts
        keytool -import -file es/root-ca.pem -alias esCA -keystore myTrustStore
        keytool -import -file minio/public.crt -alias minioCA -keystore myTrustStore
        
This creates a truststore with a root-ca that is used by default in Opendistro docker.
The path to truststore and password needs to be provided via environment variables as explained below.

#### SSL setup
Set the following environment variables:

        TRUSTSTORE_PATH=scripts/myTrustStore     # use ralative or absolute path
        TRUSTSTORE_PASSWORD=password

Note: The application binds these environment variables to system properties. 
Setting these via `application.yml` has no effect.

