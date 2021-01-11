[TOC levels=2-4 numbered]: # "Governance"

# Governance
1. [Prerequisites](#prerequisites)
1. [Development Setup](#development-setup)
   1. [Starting Database](#starting-database)
   1. [Starting RabbitMQ from SERP](#starting-rabbitmq-from-serp)
   1. [Configuring RabbitMQ for Governance](#configuring-rabbitmq-for-governance)

> **NOTE**
>
> Governance has been moved from SERP module to a dedicated Git repository and included in SERP as a dependency.

## Prerequisites

Before you start working on the project, you need to install few prerequisites.

|    Prerequisite    | Necessity | Remarks                                                                                                                                                                                                                                     |
|:------------------:|:---------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|    **Java 11**     | Mandatory | The project uses Java 11, you can [download easy to install packages for your system from Azul Systems, Inc.]( https://www.azul.com/downloads/zulu-community/?&version=java-11-lts); make sure you install full Java Development Kit (JDK). |
|     **Docker**     | Mandatory | Running integration tests requires Docker; infrastructural services also use Docker, which greatly simplifies development setup.                                                                                                            |
|    **IntelliJ**    | Mandatory | The project is prepared for development in the IntelliJ IDEA, so you have to have a license for yours.                                                                                                                                      |
| **Docker Compose** | Mandatory | Docker Compose is used to simplify setting up required infrastructural services for development.                                                                                                                                            |
|      **gdub**      | Optional  | That is a helper script for running nearest gradle wrapper. [Download it from GitHub](https://github.com/dougborg/gdub) and follow instructions for installing.                                                                             |
|      **jEnv**      | Optional  | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.                                                                                                          |
|     **Gradle**     | Optional  | The project comes with the gradle wrapper that you should use for building the project. Nevertheless, you might want to have Gradle installed.                                                                                              |

> **NOTE**
>
> All paths in this document are relative to the root project directory.

## Development Setup

Before you can run Governance, you have to have a few infrastructural services running:

1. PostgreSQL - the database for persisting data,
2. RabbitMQ - the message broker for communicating between components and with outside services.

### Starting Database

Start database service using the command:

    ./scripts/run-services.sh

> **NOTE**
>
> The above command runs `docker-compose` in compatibility mode (the `--compatibility` argument).
> This is required to limit the amount of memory to services, otherwise it would be impossible to start all the services on regular developer's computer.

### Starting RabbitMQ from SERP

The Governance uses RabbitMQ message broker to communicate with SERP, and SERP starts RabbitMQ as part of its set of infrastructural services.
Therefore, the Governance reuses the RabbitMQ that SERP maintains.

To start RabbitMQ from SERP, follow the steps:

1. Clone [the SERP repository][SERP GitLab Repository]:

       git clone git@gitlab.silenteight.com:sens/serp.git

2. Run the following commands:

       cd serp
       docker-compose --compatibility up -d rabbitmq traefik

   > **NOTE**
   >
   > You start Traefik to access RabbitMQ Management UI on the same port SERP developers access it.

3. Ensure that RabbitMQ is running:

       $ docker-compose --compatibility ps rabbitmq traefik
            Name                    Command               State   ...
       -----------------------------------------------------------...
       serp_rabbitmq_1   docker-entrypoint.sh rabbi ...   Up      ...
       serp_traefik_1    /traefik --api                   Up      ...

   The `State` column must read `Up`.

4. Log in to the [RabbitMQ Management UI](http://localhost:24111/rabbitmq) using user `serp` and password `serp`.

Once RabbitMQ and Traefik are running, you are ready to configure Governance access and exchanges.

### Configuring RabbitMQ for Governance

To configure RabbitMQ for Governance, run the following command:

    ./scripts/setup-rabbitmq.sh

You should see the command output information similar to this:

    2020-09-25 07:45:27+02:00 --- rabbitmqctl delete_user governance
    Deleting user "governance" ...
    2020-09-25 07:45:28+02:00 --- rabbitmqctl add_user governance <redacted>
    Adding user "governance" ...
    2020-09-25 07:45:29+02:00 --- rabbitmqctl set_user_tags governance monitoring management
    Setting tags for user "governance" to [monitoring, management] ...
    2020-09-25 07:45:29+02:00 --- rabbitmqctl set_permissions -p / governance .* .* .*
    Setting permissions for user "governance" in vhost "/" ...
    2020-09-25 07:45:30+02:00 --- rabbitmqctl import_definitions --format json /etc/rabbitmq/definitions.json
    Importing definitions in JSON from a file at "/etc/rabbitmq/definitions.json" ...
    Successfully started definition import. This process is asynchronous and can take some time.

> **NOTE**
>
> The setup script uses `docker exec` to run the `rabbitmqctl` command directly in the running RabbitMQ container launched for SERP.
> If you don't have the RabbitMQ container running, execution of the script will fail.

### Using Swagger UI
By default, swagger is disabled in production. To enable it you need to apply `swagger` Spring Boot profile.
UI available under [link](https://localhost:24204/rest/governance/openapi/swagger-ui/index.html?configUrl=/rest/governance/openapi/api-docs/swagger-config).

In order to make authenticated requests
click the key lock icon, type in desired client id (normally this would be `frontend`)
and click the `Authorize` button. 
