package com.silenteight.serp.governance.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.backend.common.integration.RabbitModuleConfiguration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.validation.Valid;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_COMMAND_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_RETENTION_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.GOV_EVENTS_EXCHANGE;
import static java.util.Collections.emptyMap;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BrokerProperties.class)
@Import({ RabbitModuleConfiguration.class })
class GovernanceBrokerConfiguration {

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
  Declarables modelsArchivedQueueBinding() {
    return new Declarables(
        binding(
            brokerProperties.modelsArchivedQueueName(),
            GOV_EVENTS_EXCHANGE,
            brokerProperties.modelsArchivedRoutingKey()));
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
  Queue qaRetentionAlertsExpiredQueue() {
    return queue(brokerProperties.qaRetentionAlertsExpiredQueueName());
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
  Queue governanceModelsArchivedQueue() {
    return queue(brokerProperties.modelsArchivedQueueName());
  }

  private static Queue queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .build();
  }

  @Bean
  RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  BindingRemover bindingRemover(RabbitAdmin rabbitAdmin) {
    return new BindingRemover(rabbitAdmin, brokerProperties.bindingsToRemove());
  }

  @Bean
  ExchangeRemover exchangeRemover(RabbitAdmin rabbitAdmin) {
    return new ExchangeRemover(rabbitAdmin, brokerProperties.exchangesToRemove());
  }
}
