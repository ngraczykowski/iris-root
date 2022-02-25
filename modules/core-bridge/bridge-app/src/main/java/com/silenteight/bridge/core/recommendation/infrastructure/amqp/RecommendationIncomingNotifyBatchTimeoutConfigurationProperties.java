package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.recommendation.incoming.notify-batch-timeout")
public record RecommendationIncomingNotifyBatchTimeoutConfigurationProperties(
    String queueName,
    String deadLetterQueueName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String deadLetterExchangeName,
    String exchangeName,
    String exchangeRoutingKey
) {}
