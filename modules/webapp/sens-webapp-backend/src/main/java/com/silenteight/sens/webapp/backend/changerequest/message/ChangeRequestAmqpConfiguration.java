package com.silenteight.sens.webapp.backend.changerequest.message;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ChangeRequestMessagingProperties.class)
class ChangeRequestAmqpConfiguration {

  @Bean
  TopicExchange changeRequestExchange(
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return ExchangeBuilder
        .topicExchange(changeRequestMessagingProperties.exchange())
        .build();
  }

  @Bean
  Binding changeRequestCreateBinding(
      Exchange changeRequestExchange,
      Queue changeRequestCreateQueue,
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return BindingBuilder
        .bind(changeRequestCreateQueue)
        .to(changeRequestExchange)
        .with(changeRequestMessagingProperties.routeCreate())
        .noargs();
  }

  @Bean
  Queue changeRequestCreateQueue(
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return QueueBuilder
        .durable(changeRequestMessagingProperties.queueCreate())
        .build();
  }

  @Bean
  Binding changeRequestApproveBinding(
      Exchange changeRequestExchange,
      Queue changeRequestApproveQueue,
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return BindingBuilder
        .bind(changeRequestApproveQueue)
        .to(changeRequestExchange)
        .with(changeRequestMessagingProperties.routeApprove())
        .noargs();
  }

  @Bean
  Queue changeRequestApproveQueue(
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return QueueBuilder
        .durable(changeRequestMessagingProperties.queueApprove())
        .build();
  }

  @Bean
  Binding changeRequestRejectBinding(
      Exchange changeRequestExchange,
      Queue changeRequestRejectQueue,
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return BindingBuilder
        .bind(changeRequestRejectQueue)
        .to(changeRequestExchange)
        .with(changeRequestMessagingProperties.routeReject())
        .noargs();
  }

  @Bean
  Queue changeRequestRejectQueue(
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return QueueBuilder
        .durable(changeRequestMessagingProperties.queueReject())
        .build();
  }
}
