package com.silenteight.serp.governance.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_COMMAND_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_RETENTION_EXCHANGE;
import static java.util.Collections.emptyMap;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BrokerProperties.class)
class BrokerConfiguration {

  @Valid
  @NonNull
  private final BrokerProperties brokerProperties;

  @Valid
  @NonNull
  private final ExchangeProperties exchangeProperties;

  @Bean
  Declarables analyticsBinding() {
    return new Declarables(
        binding(
            brokerProperties.analyticsQueueName(),
            exchangeProperties.getAnalytics(),
            brokerProperties.analyticsRoutingKey()));
  }

  @Bean
  Declarables ingestBinding() {
    return new Declarables(
        binding(
            brokerProperties.ingestQueueName(),
            BRIDGE_COMMAND_EXCHANGE,
            brokerProperties.ingestRoutingKey()));
  }

  @Bean
  Declarables qaRetentionPersonalInformationExpiredBinding() {
    return new Declarables(
        binding(
            brokerProperties.qaRetentionPersonalInformationExpiredQueueName(),
            BRIDGE_RETENTION_EXCHANGE,
            brokerProperties.qaRetentionPersonalInformationExpiredRoutingKey()));
  }

  @Bean
  Declarables qaRetentionAlertsExpiredBinding() {
    return new Declarables(
        binding(
            brokerProperties.qaRetentionAlertsExpiredQueueName(),
            BRIDGE_RETENTION_EXCHANGE,
            brokerProperties.qaRetentionAlertsExpiredRoutingKey()));
  }

  @Bean
  Declarables notificationBinding() {
    return new Declarables(
        binding(
            brokerProperties.notificationQueueName(),
            exchangeProperties.getNotification(),
            brokerProperties.notificationRoutingKey()));
  }

  @Bean
  Declarables notificationSendMailBinding() {
    return new Declarables(
        binding(
            brokerProperties.notificationSendMailQueueName(),
            exchangeProperties.getNotification(),
            brokerProperties.notificationSendMailRoutingKey()));
  }

  @Bean
  Declarables solutionDiscrepancyBinding() {
    return new Declarables(
        binding(
            brokerProperties.solutionDiscrepancyQueueName(),
            exchangeProperties.getSolutionDiscrepancy(),
            brokerProperties.solutionDiscrepancyRoutingKey()));
  }

  private static Binding binding(String queueName, String exchange, String routingKey) {
    return new Binding(queueName, QUEUE, exchange, routingKey, emptyMap());
  }

  @Bean
  Queue analyticsQueue() {
    return queue(brokerProperties.analyticsQueueName());
  }

  @Bean
  Queue ingestQueue() {
    return queue(brokerProperties.ingestQueueName());
  }

  @Bean
  Queue qaRetentionPersonalInformationExpiredQueue() {
    return queue(brokerProperties.qaRetentionPersonalInformationExpiredQueueName());
  }

  @Bean
  Queue notificationQueue() {
    return queue(brokerProperties.notificationQueueName());
  }

  @Bean
  Queue notificationSendMailQueue() {
    return queue(brokerProperties.notificationSendMailQueueName());
  }

  @Bean
  Queue solutionDiscrepancyQueue() {
    return queue(brokerProperties.solutionDiscrepancyQueueName());
  }

  private static Queue queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .build();
  }
}
