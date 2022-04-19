package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Conditional(AtLeastOneFacadeEnabledCondition.class)
@ConditionalOnProperty("facade.amqp.inboundExchangeName")
@EnableConfigurationProperties(AgentFacadeProperties.class)
@Profile("rabbitmq-declare")
class SingleQueueRabbitBrokerConfiguration {

  private final AgentFacadeProperties agentFacadeProperties;
  private final AmqpAdmin amqpAdmin;

  @Bean("facadeInQueue")
  Queue facadeInQueue() {
    Queue queue = QueueBuilder
        .durable(agentFacadeProperties.getInboundQueueName())
        .deadLetterExchange(agentFacadeProperties.getDeadLetterExchangeName())
        .deadLetterRoutingKey(agentFacadeProperties.getDeadLetterRoutingKey())
        .build();
    amqpAdmin.declareQueue(queue);
    return queue;
  }

  @Bean("inExchange")
  TopicExchange inExchange() {
    TopicExchange exchange = ExchangeBuilder
        .topicExchange(agentFacadeProperties.getInboundExchangeName())
        .build();
    amqpAdmin.declareExchange(exchange);
    return exchange;
  }

  @Bean("outExchange")
  TopicExchange outExchange() {
    TopicExchange exchange = ExchangeBuilder
        .topicExchange(agentFacadeProperties.getOutboundExchangeName())
        .build();
    amqpAdmin.declareExchange(exchange);
    return exchange;
  }

  @Bean
  Binding facadeInQueueBinding(
      @Qualifier("facadeInQueue") Queue facadeQueue,
      @Qualifier("inExchange") Exchange facadeExchange) {
    Binding binding = BindingBuilder
        .bind(facadeQueue)
        .to(facadeExchange)
        .with(agentFacadeProperties.getInboundRoutingKey())
        .noargs();
    amqpAdmin.declareBinding(binding);
    return binding;
  }

  @Bean("facadeDeadLetterQueue")
  Queue facadeDeadLetterQueue() {
    Queue queue = QueueBuilder
        .durable(agentFacadeProperties.getDeadLetterQueueName())
        .build();
    amqpAdmin.declareQueue(queue);
    return queue;
  }

  @Bean("facadeDeadLetterExchange")
  FanoutExchange facadeDeadLetterExchange() {
    FanoutExchange exchange = ExchangeBuilder
        .fanoutExchange(agentFacadeProperties.getDeadLetterExchangeName())
        .build();
    amqpAdmin.declareExchange(exchange);
    return exchange;
  }

  @Bean
  Binding deadLetterExchangeBinding(
      @Qualifier("facadeDeadLetterQueue") Queue facadeDlQueue,
      @Qualifier("facadeDeadLetterExchange") Exchange facadeDlExchange) {
    Binding binding = BindingBuilder
        .bind(facadeDlQueue)
        .to(facadeDlExchange)
        .with(agentFacadeProperties.getInboundRoutingKey())
        .noargs();
    amqpAdmin.declareBinding(binding);
    return binding;
  }
}
