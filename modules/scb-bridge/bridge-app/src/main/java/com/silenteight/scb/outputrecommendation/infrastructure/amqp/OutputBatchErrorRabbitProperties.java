package com.silenteight.scb.outputrecommendation.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.output-recommendation.incoming.batch-error")
public record OutputBatchErrorRabbitProperties(
    String exchangeName,
    String queueName,
    String deadLetterQueueName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String deadLetterExchangeName) {
}
