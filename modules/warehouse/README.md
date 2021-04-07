# Silent Eight Warehouse

## Configuration management

There are multiple yml configuration files in the `warehouse-app/src/main/resources/config`:

- `application.yml` contains all application-related configuration
- `application-local.yml` contains all deployment-related configuration required to run
the application locally. This file can be considered to be a base for creating *.yml configuration
for any other deployments, e.g. nomad deployment stored in `nomad/conf/application.yml`  
- `application-dev.yml` contains configuration convenient for development 
such as logging, pretty-print, etc.
- `application-swagger.yml` enables and configures swagger
- `application-tls.yml` enables and configures tls

## Development Setup

Before you can run Warehouse, you have to have a few infrastructural services running:

1. RabbitMQ - the message broker for communicating between components and with outside services.

### Starting RabbitMQ 
Warehouse uses RabbitMQ message broker to communicate with other components.

To start RabbitMQ, follow the steps:

1. Clone [Common Docker Infrastructure]:

       git clone https://gitlab.silenteight.com/sens/common-docker-infrastructure
       
1. Start docker-compose. It is sufficient to start just the rabbitmq service.
    
       docker-compose up -d rabbitmq

### Starting ElasticSearch
Warehouse uses elastic search as a persistence store.

To start elasticsearch run:

        ./dev-elk/start.sh
        
If you need to run elasticsearch with TLS enabled:

        ./dev-elk/start-https.sh
              
## Testing

### Swagger-ui

In order to run the application with swagger enabled apply 'swagger' Spring profile.

Swagger UI is accessible via [http](http://localhost:24900/rest/warehouse/openapi/swagger-ui/index.html?configUrl=/rest/warehouse/openapi/api-docs/swagger-config).

## Other

### Setting-up HTTPS for ES connection in dev environment 

#### Truststore
There is a test truststore in `./dev-elk`. You can re-create it by following the steps below:

        cd dev-elk
        keytool -import -file root-ca.pem -alias esCA -keystore myTrustStore
        
This creates a truststore with a root-ca that is used by default in OpenDistro docker.
The path to truststore and password needs to be provided via environment variables as explained below.

#### SSL setup
Set the following environment variables:

        TRUSTSTORE_PATH=dev-elk/myTrustStore     # use ralative or absolute path
        TRUSTSTORE_PASSWORD=password

Note: The application binds these environment variables to system properties. 
Setting these via `application.yml` has no effect.

