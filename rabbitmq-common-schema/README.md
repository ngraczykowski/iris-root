# RabbitMQ Common Schema

This project holds a library with RabbitMQ schema definition of common broker infrastructure
exchanges. The initializr is an application that simply pulls in the library, connects to RabbitMQ,
and applies the common schema.

## Develop

Before you start you need to run you local rabbitmq instance to be able to connect and develop

```bash
docker-compose up -d
```

Now you are able to run application from InteliJ IDEA

## How to apply commons into project

To include commons RabbitMQ library you need to add line to gradle project.

Requirements:

* Spring Boot 2.2+
* include spring-amqp dependency (minimum 2.1)
* include spring-rabbit dependency (minimum 2.1)
* use @EnableAutoConfiguration inside project

```groovy
implementation(group: 'com.silenteight.rabbitmqcommonschema', name: 'sear-rabbitmq-common-schema-definitions', version: '1.0.0')

//required dependencies
implementation group: 'org.springframework.amqp', name: 'spring-amqp', version: '<version>'
implementation group: 'org.springframework.amqp', name: 'spring-rabbit', version: '<version>'
```

## How it Works

After booting up rabbitmq initializr application creates a connection to RabbitMQ and all defined
exchanges will be created inside RabbitMQ instance. Application will be closed just after setup will
be accomplished.

n k8s cluster application has been defined as Job, so it's executed once and then marked as
completed.

## Helm Chart

Helm Chart is a release package which is automatically versioned when deployed on Repository.
Deployment of an Application Chart is executed by environment specific project
