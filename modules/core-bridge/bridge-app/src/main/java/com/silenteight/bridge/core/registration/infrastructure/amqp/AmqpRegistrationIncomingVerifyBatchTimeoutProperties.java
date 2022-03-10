package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("amqp.registration.incoming.verify-batch-timeout")
public record AmqpRegistrationIncomingVerifyBatchTimeoutProperties(
    String queueName,
    String deadLetterQueueName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String deadLetterExchangeName,
    Duration delayTime
) {}
