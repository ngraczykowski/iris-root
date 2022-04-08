package com.silenteight.bridge.core.reports.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.reports.incoming.batch-delivered")
public record ReportsIncomingBatchDeliveredProperties(
    String exchangeName,
    String queueName,
    String deadLetterQueueName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String deadLetterExchangeName,
    String exchangeRoutingKey
) {}
