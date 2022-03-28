# Agent Facade

## Running an agent

Each agent should be run with the following settings to make use of the Facade:
1. Spring Boot profiles: `dev,rabbitmq-declare`.
2. Property `*.agent.facade.enabled` set to `true` in `bootstrap.properties` in **the agent project.**

## Testing

1. Prerequisite: RabbitMQ is up and running in Docker. You may use the one from docker-compose, similar RabbitMQ configuration is present in common-docker-infrastructure.
   The credentials to the RabbitMQ console are **dev/dev**.
2. Run an agent Spring Boot application in a debug mode. with spring profiles `dev,rabbitmq-declare` and property `*.agent.facade.enabled` set to `true` (located in `bootstrap.properties`). 
   The new queues and exchanges should be created in Rabbit.
3. Send a message to the inbound queue using a JShell Console snippet https://gitlab.silenteight.com/ro/agent-facade/-/snippets/240 (you might need to change `exchange request`, `credentials` and `basicPublish() args`).
4. Message should be processed by agent's facade. Each facade with `dev` mode has configured mocked data source that responds with static data (you can inspect `DataSourceClientWithStaticContent` class for each agent).
5. Validate in a debugger if there is a proper message received. Also, if it is sent back to the output exchange.
6. Validate in RabbitMQ console if there are no messages on the input queue. (`http://localhost:24161/rabbitmq`, user/pwd: `dev/dev`)

## Multi-queue agents

By default, each agent has only 1 facade and listen only to single queue.

It's configured by:
```
facade.amqp.inboundExchangeName=agent.request
facade.amqp.inboundQueueName=<name of inbound queue>
facade.amqp.inboundRoutingKey=<routing key>
```

Each agent could have multiple facades and listen to multiple queues.
To connect more facades within a single agent, do the following:

1. Delete or comment settings for single queue definition:
```asciidoc
facade.amqp.inboundExchangeName
facade.amqp.inboundQueueName
facade.amqp.inboundRoutingKey
facade.amqp.outboundExchangeName
facade.amqp.deadLetterExchangeName
facade.amqp.deadLetterQueueName
facade.amqp.deadLetterRoutingKey
```

2. Enable multi-queue by adding:

```
facade.amqp.multi-queues.enabled=true
```

3. Define queue definitions per each facade you would like to handle.

Assuming that you want to provide AMQP and integration flow configurations for the following facades:

```java
public class FirstFacadeAmqp extends AbstractAgentFacade<FirstInputsRequest, FirstInput> {

  @Override
  public AgentExchangeResponse processMessage(AgentExchangeRequest request) {
    // your processor
  }

  @Override
  public String getFacadeName() {
    return "first";
  }
}


public class SecondFacadeAmqp extends AbstractAgentFacade<SecondInputsRequest, SecondInput> {

  @Override
  public AgentExchangeResponse processMessage(AgentExchangeRequest request) {
    // your processor
  }

  @Override
  public String getFacadeName() {
    return "second";
  }
}

```

you need to provide following configuration:

```
facade.amqp.multi-queues.enabled=true
facade.amqp.queueDefinitions[first].inbound-exchange-name=<first-facade-inbound-exchange-name>
facade.amqp.queueDefinitions[first].inbound-queue-name=<first-facade-inbound-queue-name>
facade.amqp.queueDefinitions[first].inbound-routing-key=<first-facade-inbound-routing-key>
facade.amqp.queueDefinitions[first].outbound-exchange-name=<first-facade-outbound-exchange-name>
facade.amqp.queueDefinitions[first].dead-letter-exchange-name=<first-facade-dead-letter-exchange-name>
facade.amqp.queueDefinitions[first].dead-letter-queue-name=<first-facade-dead-letter-queue-name>
facade.amqp.queueDefinitions[first].dead-letter-routing-key=<first-facade-dead-letter-routing-key>
facade.amqp.queueDefinitions[second].inbound-exchange-name=<second-facade-inbound-exchange-name>
facade.amqp.queueDefinitions[second].inbound-queue-name=<second-facade-inbound-queue-name>
facade.amqp.queueDefinitions[second].inbound-routing-key=<second-facade-inbound-routing-key>
facade.amqp.queueDefinitions[second].outbound-exchange-name=<second-facade-outbound-exchange-name>
facade.amqp.queueDefinitions[second].dead-letter-exchange-name=<second-facade-dead-letter-exchange-name>
facade.amqp.queueDefinitions[second].dead-letter-queue-name=<second-facade-dead-letter-queue-name>
facade.amqp.queueDefinitions[second].dead-letter-routing-key=<second-facade-dead-letter-routing-key>
```

## Parallelism

Facade's `AgentFacade.processMessage()` method can process data using ForkJoinPool and parallel stream. To activate it
one needs to set `facade.amqp.parallelism` to value greater than 1