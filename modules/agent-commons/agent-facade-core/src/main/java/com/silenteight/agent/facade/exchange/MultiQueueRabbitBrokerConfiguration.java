package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty({ "agent.facade.enabled", "facade.amqp.multi-queues.enabled" })
@EnableConfigurationProperties(AgentFacadeProperties.class)
@Profile("rabbitmq-declare")
class MultiQueueRabbitBrokerConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;
  private final AmqpAdmin amqpAdmin;

  @PostConstruct
  public void init() {
    agentFacadeProperties.getQueueDefinitions()
        .values()
        .stream()
        .forEach(this::createQueuesAndExchangesAndBindings);
  }

  private void createQueuesAndExchangesAndBindings(QueueItem queueItem) {
    var inboundRoutingKey = queueItem.getInboundRoutingKey();
    var deadLetterExchangeName = queueItem.getDeadLetterExchangeName();

    var inboundQueue = createQueue(queueItem.getInboundQueueName(),
        deadLetterExchangeName, queueItem.getDeadLetterRoutingKey());
    var inboundExchange = createTopicExchange(queueItem.getInboundExchangeName());
    createQueueBinding(inboundQueue, inboundExchange, inboundRoutingKey);

    createTopicExchange(queueItem.getOutboundExchangeName());

    var deadLetterQueue = createQueue(queueItem.getDeadLetterQueueName());
    var deadLetterExchange = createFanoutExchange(deadLetterExchangeName);
    createQueueBinding(deadLetterQueue, deadLetterExchange, inboundRoutingKey);
  }
  
  private Queue createQueue(
      String queueName, String deadLetterExchange, String deadLetterRoutingKey) {

    var queue = QueueBuilder
        .durable(queueName)
        .deadLetterExchange(deadLetterExchange)
        .deadLetterRoutingKey(deadLetterRoutingKey)
        .build();
    amqpAdmin.declareQueue(queue);
    return queue;
  }

  private TopicExchange createTopicExchange(String exchangeName) {
    TopicExchange exchange = ExchangeBuilder
        .topicExchange(exchangeName)
        .build();
    amqpAdmin.declareExchange(exchange);
    return exchange;
  }

  private void createQueueBinding(Queue queue, Exchange exchange, String routingKey) {
    Binding binding = BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
    amqpAdmin.declareBinding(binding);
  }

  private Queue createQueue(String queueName) {
    Queue queue = QueueBuilder
        .durable(queueName)
        .build();
    amqpAdmin.declareQueue(queue);
    return queue;
  }

  private FanoutExchange createFanoutExchange(String exchangeName) {
    FanoutExchange exchange = ExchangeBuilder
        .fanoutExchange(exchangeName)
        .build();
    amqpAdmin.declareExchange(exchange);
    return exchange;
  }
}
