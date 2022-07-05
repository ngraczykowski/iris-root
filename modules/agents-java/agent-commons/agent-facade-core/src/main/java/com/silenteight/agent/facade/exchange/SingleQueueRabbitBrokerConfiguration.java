package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Conditional(SingleFacadeEnabledCondition.class)
@EnableConfigurationProperties(AgentFacadeProperties.class)
@Profile("rabbitmq-declare")
class SingleQueueRabbitBrokerConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;

  @Bean("facadeInQueue")
  Queue facadeInQueue() {
    return QueueBuilder
        .durable(agentFacadeProperties.getInboundQueueName())
        .deadLetterExchange(agentFacadeProperties.getDeadLetterExchangeName())
        .deadLetterRoutingKey(agentFacadeProperties.getDeadLetterRoutingKey())
        .build();
  }

  @Bean("facadeInQueueWithPrioritySupport")
  Queue facadeInQueueWithPrioritySupport() {
    return QueueBuilder
        .durable(agentFacadeProperties.getInboundQueueWithPrioritySupportName())
        .deadLetterExchange(agentFacadeProperties.getDeadLetterExchangeName())
        .deadLetterRoutingKey(agentFacadeProperties.getDeadLetterRoutingKey())
        .maxPriority(agentFacadeProperties.getMaxQueuePriority())
        .build();
  }

  @Bean("inExchange")
  TopicExchange inExchange() {
    return ExchangeBuilder
        .topicExchange(agentFacadeProperties.getInboundExchangeName())
        .build();
  }

  @Bean("outExchange")
  TopicExchange outExchange() {
    return ExchangeBuilder
        .topicExchange(agentFacadeProperties.getOutboundExchangeName())
        .build();
  }

  @Bean("facadeInQueueBinding")
  Binding facadeInQueueBinding(
      @Qualifier("facadeInQueue") Queue facadeQueue,
      @Qualifier("inExchange") Exchange facadeExchange) {
    return BindingBuilder
        .bind(facadeQueue)
        .to(facadeExchange)
        .with(agentFacadeProperties.getInboundRoutingKey())
        .noargs();
  }

  @Bean("facadeInQueueWithPrioritySupportBinding")
  Binding facadeInQueueWithPrioritySupportBinding(
      @Qualifier("facadeInQueueWithPrioritySupport") Queue queueWithPrioSupport,
      @Qualifier("inExchange") Exchange facadeExchange) {
    return BindingBuilder
        .bind(queueWithPrioSupport)
        .to(facadeExchange)
        .with(agentFacadeProperties.getInboundRoutingKey())
        .noargs();
  }

  @Bean("facadeDeadLetterQueue")
  Queue facadeDeadLetterQueue() {
    return QueueBuilder
        .durable(agentFacadeProperties.getDeadLetterQueueName())
        .build();
  }

  @Bean("facadeDeadLetterExchange")
  FanoutExchange facadeDeadLetterExchange() {
    return ExchangeBuilder
        .fanoutExchange(agentFacadeProperties.getDeadLetterExchangeName())
        .build();
  }

  @Bean
  Binding deadLetterExchangeBinding(
      @Qualifier("facadeDeadLetterQueue") Queue facadeDlQueue,
      @Qualifier("facadeDeadLetterExchange") Exchange facadeDlExchange) {
    return BindingBuilder
        .bind(facadeDlQueue)
        .to(facadeDlExchange)
        .with(agentFacadeProperties.getInboundRoutingKey())
        .noargs();
  }
}
