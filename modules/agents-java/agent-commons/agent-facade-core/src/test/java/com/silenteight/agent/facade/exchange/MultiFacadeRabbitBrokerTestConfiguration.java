package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.DependsOn;

@Conditional(MultiFacadeEnabledCondition.class)
@TestConfiguration
@EnableConfigurationProperties(AgentFacadeProperties.class)
@RequiredArgsConstructor
@DependsOn("multiQueueRabbitBrokerConfiguration")
public class MultiFacadeRabbitBrokerTestConfiguration {

  static final String TEST_FACADE_OUT_QUEUE = "agent.outQueue";
  private final AgentFacadeProperties agentFacadeProperties;
  private final AmqpAdmin amqpAdmin;

  @Bean
  Binding createOutExchangeBindings() {
    var binding = BindingBuilder
        .bind(facadeOutQueue())
        .to(outExchange())
        .with("");
    amqpAdmin.declareBinding(binding);
    return binding;
  }

  private Queue facadeOutQueue() {
    var queue = QueueBuilder
        .durable(TEST_FACADE_OUT_QUEUE)
        .build();
    amqpAdmin.declareQueue(queue);
    return queue;
  }

  private TopicExchange outExchange() {
    TopicExchange exchange = ExchangeBuilder
        .topicExchange(agentFacadeProperties.getOutboundExchangeName())
        .build();
    amqpAdmin.declareExchange(exchange);
    return exchange;
  }

}
