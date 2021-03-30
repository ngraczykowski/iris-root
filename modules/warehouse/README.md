# Silent Eight Warehouse

## Development Setup

Before you can run Warehouse, you have to have a few infrastructural services running:

1. RabbitMQ - the message broker for communicating between components and with outside services.

### Starting RabbitMQ 
Governance uses RabbitMQ message broker to communicate with other components.

To start RabbitMQ, follow the steps:

1. Clone [Common Docker Infrastructure]:

       git clone https://gitlab.silenteight.com/sens/common-docker-infrastructure
       
1. Start docker-compose. It is sufficient to start just the rabbitmq service.
    
       docker-compose up -d rabbitmq

## Testing

### Swagger-ui

In order to run the application with swagger enabled apply 'swagger' Spring profile.

Swagger UI is accessible via [http](http://localhost:24900/rest/warehouse/openapi/swagger-ui/index.html?configUrl=/rest/warehouse/openapi/api-docs/swagger-config).
