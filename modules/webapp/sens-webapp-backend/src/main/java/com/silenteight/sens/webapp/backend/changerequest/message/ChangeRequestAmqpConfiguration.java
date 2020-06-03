package com.silenteight.sens.webapp.backend.changerequest.message;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChangeRequestAmqpConfiguration {

  @Bean
  TopicExchange changeRequestExchange(
      @Value("${messaging.exchange.change-request}") String changeRequestExchangeName) {
    return ExchangeBuilder
        .topicExchange(changeRequestExchangeName)
        .build();
  }

  @Bean
  Binding changeRequestCreateBinding(
      Exchange changeRequestExchange,
      Queue changeRequestCreateQueue,
      @Value("${messaging.route.change-request.create}") String routeChangeRequestCreate) {
    return BindingBuilder
        .bind(changeRequestCreateQueue)
        .to(changeRequestExchange)
        .with(routeChangeRequestCreate)
        .noargs();
  }

  @Bean
  Queue changeRequestCreateQueue(
      @Value("${messaging.queue.change-request.create}") String changeRequestCreateQueueName) {
    return QueueBuilder
        .durable(changeRequestCreateQueueName)
        .build();
  }

  @Bean
  Binding changeRequestApproveBinding(
      Exchange changeRequestExchange,
      Queue changeRequestApproveQueue,
      @Value("${messaging.route.change-request.approve}") String routeChangeRequestApprove) {
    return BindingBuilder
        .bind(changeRequestApproveQueue)
        .to(changeRequestExchange)
        .with(routeChangeRequestApprove)
        .noargs();
  }

  @Bean
  Queue changeRequestApproveQueue(
      @Value("${messaging.queue.change-request.approve}") String changeRequestApproveQueueName) {
    return QueueBuilder
        .durable(changeRequestApproveQueueName)
        .build();
  }

  @Bean
  Binding changeRequestRejectBinding(
      Exchange changeRequestExchange,
      Queue changeRequestRejectQueue,
      @Value("${messaging.route.change-request.reject}") String routeChangeRequestReject) {
    return BindingBuilder
        .bind(changeRequestRejectQueue)
        .to(changeRequestExchange)
        .with(routeChangeRequestReject)
        .noargs();
  }

  @Bean
  Queue changeRequestRejectQueue(
      @Value("${messaging.queue.change-request.reject}") String changeRequestRejectQueueName) {
    return QueueBuilder
        .durable(changeRequestRejectQueueName)
        .build();
  }
}
