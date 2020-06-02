package com.silenteight.sens.webapp.backend.changerequest.message;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChangeRequestAmqpConfiguration {

  @Bean
  TopicExchange changeRequestExchange() {
    return ExchangeBuilder
        .topicExchange(ChangeRequestAmqpDefaults.EXCHANGE_CHANGE_REQUEST)
        .build();
  }

  @Bean
  Binding changeRequestCreateBinding(Exchange changeRequestExchange) {
    return BindingBuilder
        .bind(changeRequestCreateQueue())
        .to(changeRequestExchange)
        .with(ChangeRequestAmqpDefaults.ROUTE_CHANGE_REQUEST_CREATE)
        .noargs();
  }

  @Bean
  Queue changeRequestCreateQueue() {
    return QueueBuilder
        .durable(ChangeRequestAmqpDefaults.CHANGE_REQUEST_CREATE_QUEUE)
        .build();
  }

  @Bean
  Binding changeRequestApproveBinding(Exchange changeRequestExchange) {
    return BindingBuilder
        .bind(changeRequestApproveQueue())
        .to(changeRequestExchange)
        .with(ChangeRequestAmqpDefaults.ROUTE_CHANGE_REQUEST_APPROVE)
        .noargs();
  }

  @Bean
  Queue changeRequestApproveQueue() {
    return QueueBuilder
        .durable(ChangeRequestAmqpDefaults.CHANGE_REQUEST_APPROVE_QUEUE)
        .build();
  }

  @Bean
  Binding changeRequestRejectBinding(Exchange changeRequestExchange) {
    return BindingBuilder
        .bind(changeRequestRejectQueue())
        .to(changeRequestExchange)
        .with(ChangeRequestAmqpDefaults.ROUTE_CHANGE_REQUEST_REJECT)
        .noargs();
  }

  @Bean
  Queue changeRequestRejectQueue() {
    return QueueBuilder
        .durable(ChangeRequestAmqpDefaults.CHANGE_REQUEST_REJECT_QUEUE)
        .build();
  }
}
