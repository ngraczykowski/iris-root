# Silent Eight Simulator

## Development Setup

Before you can run Simulator, you have to have a few infrastructural services running:

1. PostgreSQL - the database for persisting data,
2. RabbitMQ - the message broker for communicating between components and with outside services.

### Starting Database

Start database service using the command:

    ./scripts/run-services.sh

### Starting RabbitMQ 
Simulator uses RabbitMQ message broker to communicate with other components.

To start RabbitMQ, follow the steps:

1. Clone [Common Docker Infrastructure]:

       git clone https://gitlab.silenteight.com/sens/common-docker-infrastructure
       
1. Start docker-compose. It is sufficient to start just the rabbitmq service.
    
       docker-compose up -d rabbitmq
