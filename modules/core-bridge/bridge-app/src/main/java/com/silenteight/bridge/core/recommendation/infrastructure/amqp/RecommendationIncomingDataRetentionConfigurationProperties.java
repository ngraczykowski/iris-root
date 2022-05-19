package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.recommendation.incoming.data-retention")
public record RecommendationIncomingDataRetentionConfigurationProperties(
    String exchangeName,
    String alertsExpiredQueueName,
    String alertsExpiredRoutingKey,
    String alertsExpiredDeadLetterQueueName,
    String alertsExpiredDeadLetterExchangeName,
    Integer deadLetterQueueTimeToLiveInMilliseconds) {}
