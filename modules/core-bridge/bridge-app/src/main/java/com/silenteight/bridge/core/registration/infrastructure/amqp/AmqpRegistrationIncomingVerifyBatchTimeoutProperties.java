package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("amqp.registration.incoming.verify-batch-timeout")
public record AmqpRegistrationIncomingVerifyBatchTimeoutProperties(
    String delayedQueueName,
    String queueName,
    String errorAlertsQueueName,
    Integer queueTimeToLiveInMilliseconds,
    String deadLetterExchangeName,
    Duration delayTime
) {}
