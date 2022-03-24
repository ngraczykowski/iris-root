package com.silenteight.scb.outputrecommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.output-recommendation.incoming.notify-batch-completed")
public record OutputNotifyBatchCompletedRabbitProperties(
    String exchangeName,
    String queueName,
    String deadLetterQueueName,
    String deadLetterExchangeName,
    Integer deadLetterQueueTimeToLiveInMilliseconds
) {}
