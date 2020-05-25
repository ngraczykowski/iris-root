package com.silenteight.serp.common.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.silenteight.serp.common.messaging.MessagingConstants.*;

@Configuration
class BrokerConfiguration {

  //INFO(kdzieciol): positive integer between 1 and 255, values between 1 and 10 are recommended
  private static final int QUEUE_MAX_PRIORITY = 10;

  private static final String X_MESSAGE_TTL_ARG = "x-message-ttl";
  private static final String X_QUEUE_MODE_ARG = "x-queue-mode";
  private static final String X_MAX_PRIORITY_ARG = "x-max-priority";

  @Bean
  Declarables alertMetadataBinding(DirectExchange alertMetadata) {
    return new Declarables(
        BindingBuilder
            .bind(governanceDecisionGroupsQueue())
            .to(alertMetadata)
            .with(ROUTE_ALERT_DECISION_GROUPS),
        BindingBuilder
            .bind(governancePipelineFeatureGroupsQueue())
            .to(alertMetadata)
            .with(ROUTE_PIPELINE_FEATURE_GROUPS),
        BindingBuilder
            .bind(governancePipelineModelQueue())
            .to(alertMetadata)
            .with(ROUTE_PIPELINE_MODEL),
        BindingBuilder
            .bind(indexerPipelineModelQueue())
            .to(alertMetadata)
            .with(ROUTE_PIPELINE_MODEL),
        BindingBuilder
            .bind(indexerAlertModelQueue())
            .to(alertMetadata)
            .with(ROUTE_ALERT_MODEL),
        BindingBuilder
            .bind(recoPipelineModelQueue())
            .to(alertMetadata)
            .with(ROUTE_PIPELINE_MODEL)
    );
  }

  @Bean
  Declarables alertOrdersBinding(DirectExchange alertOrders) {
    return new Declarables(
        BindingBuilder.bind(storeOrderQueue()).to(alertOrders).with(ROUTE_ORDER_CACHED),
        BindingBuilder.bind(recoRejectedOrderQueue()).to(alertOrders).with(ROUTE_ORDER_REJECTED));
  }

  @Bean
  Binding reportDataBinding(TopicExchange reportData) {
    return BindingBuilder
        .bind(reporterInfoQueue())
        .to(reportData)
        .with(ROUTE_INFO_DESCRIPTIONS);
  }

  @Bean
  Declarables alertUnprocessedBinding(FanoutExchange alertUnprocessed) {
    return new Declarables(
        BindingBuilder.bind(pipelineAlertQueue()).to(alertUnprocessed));
  }

  @Bean
  Declarables alertProcessedBinding(FanoutExchange alertProcessed) {
    // TODO(ahaczewski): Uncomment or move indexer queue binding.
    return new Declarables(
        //BindingBuilder.bind(indexerAlertQueue()).to(alertProcessed),
        BindingBuilder.bind(recoAlertQueue()).to(alertProcessed));
  }

  @Bean
  Binding alertRecommendationBinding(DirectExchange alertRecommendationExchange) {
    return BindingBuilder
        .bind(gatewayRecommendationQueue())
        .to(alertRecommendationExchange)
        .with(ROUTE_ALERT_RECOMMENDATION);
  }

  @Bean
  Declarables messageNotRoutedBinding(FanoutExchange alertNotRoutedExchange) {
    return new Declarables(
        BindingBuilder.bind(messageNotRoutedQueue()).to(alertNotRoutedExchange));
  }

  @Bean
  Declarables notificationBinding(TopicExchange notificationExchange) {
    return new Declarables(
        BindingBuilder
            .bind(notifierIncomingEventQueue())
            .to(notificationExchange)
            .with("notification.*")
    );
  }

  @Bean
  Declarables learningBinding(TopicExchange learningExchange) {
    return new Declarables(
        BindingBuilder
            .bind(governanceSolutionDiscrepancyQueue())
            .to(learningExchange)
            .with(ROUTE_DISCREPANCY_DETECTED)
    );
  }

  @Bean
  Queue pipelineAlertQueue() {
    return QueueBuilder
        .durable("pipeline.alert")
        .withArgument(X_MAX_PRIORITY_ARG, QUEUE_MAX_PRIORITY)
        .build();
  }

  @Bean
  Queue notifierIncomingEventQueue() {
    return QueueBuilder.durable("notifier.incoming-event").build();
  }

  @Bean
  Queue notifierSendMailQueue() {
    return QueueBuilder.durable("notifier.send-mail").build();
  }

  @Bean
  Queue recoAlertQueue() {
    return QueueBuilder
        .durable("reco.alert")
        .withArgument(X_MAX_PRIORITY_ARG, QUEUE_MAX_PRIORITY)
        .build();
  }

  @Bean
  Queue indexerAlertQueue() {
    return QueueBuilder.durable("indexer.alert").build();
  }

  @Bean
  Queue storeOrderQueue() {
    return QueueBuilder.durable("store.order").build();
  }

  @Bean
  Queue recoRejectedOrderQueue() {
    return QueueBuilder.durable("reco.rejected-order").build();
  }

  @Bean
  Queue governanceDecisionGroupsQueue() {
    return QueueBuilder.durable("governance.decision-groups").build();
  }

  @Bean
  Queue governancePipelineModelQueue() {
    return QueueBuilder.durable("governance.pipeline-model").build();
  }

  @Bean
  Queue indexerPipelineModelQueue() {
    return QueueBuilder.durable("indexer.pipeline-model").build();
  }

  @Bean
  Queue recoPipelineModelQueue() {
    return QueueBuilder.durable("reco.pipeline-model").build();
  }

  @Bean
  Queue indexerAlertModelQueue() {
    return QueueBuilder.durable("indexer.alert-model").build();
  }

  @Bean
  Queue governancePipelineFeatureGroupsQueue() {
    return QueueBuilder.durable("governance.pipeline-feature-groups").build();
  }

  @Bean
  Queue reporterInfoQueue() {
    return QueueBuilder.durable("reporter.info").build();
  }

  @Bean
  Queue governanceSolutionDiscrepancyQueue() {
    return QueueBuilder.durable("governance.solution-discrepancy").build();
  }

  @Bean
  Queue gatewayRecommendationQueue() {
    return QueueBuilder
        .durable("gateway.alert-recommendation")
        .withArgument(X_MAX_PRIORITY_ARG, QUEUE_MAX_PRIORITY)
        .build();
  }

  @Bean
  Queue messageNotRoutedQueue() {
    return QueueBuilder
        .durable("message.not-routed")
        .withArgument(X_MESSAGE_TTL_ARG, Duration.ofMinutes(60).toMillis())
        .withArgument(X_QUEUE_MODE_ARG, "lazy")
        .build();
  }
}
