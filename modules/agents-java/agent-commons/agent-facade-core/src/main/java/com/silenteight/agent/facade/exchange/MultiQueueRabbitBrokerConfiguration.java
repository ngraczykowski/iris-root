package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Configuration
@RequiredArgsConstructor
@Conditional(MultiFacadeEnabledCondition.class)
@EnableConfigurationProperties(AgentFacadeProperties.class)
@Profile("rabbitmq-declare")
class MultiQueueRabbitBrokerConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;

  @Bean("multiFacadeDeclarables")
  public Declarables init() {
    var declarables = agentFacadeProperties
        .getQueueDefinitions()
        .values()
        .stream()
        .flatMap(MultiQueueRabbitBrokerConfiguration::createQueuesAndExchangesAndBindings)
        .collect(toList());
    return new Declarables(declarables);
  }

  private static Stream<Declarable> createQueuesAndExchangesAndBindings(QueueItem queueItem) {
    var inboundRoutingKey = queueItem.getInboundRoutingKey();
    var deadLetterExchangeName = queueItem.getDeadLetterExchangeName();
    var deadLetterRoutingKey = queueItem.getDeadLetterRoutingKey();

    var inboundQueue = createQueue(queueItem.getInboundQueueName(), deadLetterExchangeName,
        deadLetterRoutingKey);
    var inboundQueueWithPrioritySupport =
        createQueueWithPrioritySupport(queueItem.getInboundQueueWithPrioritySupportName(),
            deadLetterExchangeName, deadLetterRoutingKey, queueItem.getMaxQueuePriority());

    var inboundExchange = createTopicExchange(queueItem.getInboundExchangeName());

    var queueBinding = createQueueBinding(inboundQueue, inboundExchange, inboundRoutingKey);
    var queueWithPrioritySupportBinding =
        createQueueBinding(inboundQueueWithPrioritySupport, inboundExchange, inboundRoutingKey);

    var outboundExchange = createTopicExchange(queueItem.getOutboundExchangeName());

    var deadLetterQueue = createQueue(queueItem.getDeadLetterQueueName());
    var deadLetterExchange = createFanoutExchange(deadLetterExchangeName);
    var deadLetterBinding =
        createQueueBinding(deadLetterQueue, deadLetterExchange, inboundRoutingKey);

    return Stream.of(inboundQueue, inboundQueueWithPrioritySupport, inboundExchange,
        queueBinding, queueWithPrioritySupportBinding, outboundExchange,
        deadLetterQueue, deadLetterExchange, deadLetterBinding);
  }

  private static Queue createQueueWithPrioritySupport(
      String queueName, String deadLetterExchange, String deadLetterRoutingKey, int priority) {
    return QueueBuilder
        .durable(queueName)
        .deadLetterExchange(deadLetterExchange)
        .deadLetterRoutingKey(deadLetterRoutingKey)
        .maxPriority(priority)
        .build();
  }

  private static Queue createQueue(
      String queueName, String deadLetterExchange, String deadLetterRoutingKey) {

    return QueueBuilder
        .durable(queueName)
        .deadLetterExchange(deadLetterExchange)
        .deadLetterRoutingKey(deadLetterRoutingKey)
        .build();
  }

  private static TopicExchange createTopicExchange(String exchangeName) {
    return ExchangeBuilder.topicExchange(exchangeName).build();
  }

  private static Binding createQueueBinding(Queue queue, Exchange exchange, String routingKey) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
  }

  private static Queue createQueue(String queueName) {
    return QueueBuilder.durable(queueName).build();
  }

  private static FanoutExchange createFanoutExchange(String exchangeName) {
    return ExchangeBuilder.fanoutExchange(exchangeName).build();
  }
}
