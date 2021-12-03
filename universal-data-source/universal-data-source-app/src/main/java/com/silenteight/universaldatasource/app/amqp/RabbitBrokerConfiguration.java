package com.silenteight.universaldatasource.app.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_RETENTION_EXCHANGE;
import static com.silenteight.universaldatasource.common.messaging.RabbitQueueCommons.UDS_CATEGORY_VALUE_RETENTION_QUEUE_NAME;
import static com.silenteight.universaldatasource.common.messaging.RabbitQueueCommons.UDS_COMMENT_INPUT_RETENTION_QUEUE_NAME;
import static com.silenteight.universaldatasource.common.messaging.RabbitQueueCommons.UDS_FEATURE_INPUT_RETENTION_QUEUE_NAME;

@Configuration
class RabbitBrokerConfiguration {

  public static final String DATA_RETENTION_ALERT_EXPIRED_ROUTING_KEY =
      "retention.alerts-expired";
  public static final String X_QUEUE_TYPE_KEY = "x-queue-type";
  public static final String X_QUEUE_TYPE_VALUE =  "classic";

  @Bean
  Declarables rabbitBrokerDeclarables() {

    var categoryValueRetentionQueue = queue(UDS_CATEGORY_VALUE_RETENTION_QUEUE_NAME).build();
    var categoryValueRetentionBinding =
        bind(UDS_CATEGORY_VALUE_RETENTION_QUEUE_NAME, BRIDGE_RETENTION_EXCHANGE);

    var featureInputRetentionQueue = queue(UDS_FEATURE_INPUT_RETENTION_QUEUE_NAME).build();
    var featureInputRetentionBinding =
        bind(UDS_FEATURE_INPUT_RETENTION_QUEUE_NAME, BRIDGE_RETENTION_EXCHANGE);

    var commentInputRetentionQueue = queue(UDS_COMMENT_INPUT_RETENTION_QUEUE_NAME).build();
    var commentInputRetentionBinding =
        bind(UDS_COMMENT_INPUT_RETENTION_QUEUE_NAME, BRIDGE_RETENTION_EXCHANGE);

    return new Declarables(
        categoryValueRetentionQueue, categoryValueRetentionBinding,
        featureInputRetentionQueue, featureInputRetentionBinding,
        commentInputRetentionQueue, commentInputRetentionBinding);
  }

  private static QueueBuilder queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .withArgument(X_QUEUE_TYPE_KEY, X_QUEUE_TYPE_VALUE);
  }

  private static Binding bind(String queueName, String exchangeName) {
    return new Binding(
        queueName, DestinationType.QUEUE, exchangeName,
        DATA_RETENTION_ALERT_EXPIRED_ROUTING_KEY, null);
  }
}

