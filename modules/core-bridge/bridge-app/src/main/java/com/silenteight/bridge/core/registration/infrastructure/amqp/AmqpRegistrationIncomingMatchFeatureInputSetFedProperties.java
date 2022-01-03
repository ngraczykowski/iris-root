package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.registration.incoming.match-feature-input-set-fed")
record AmqpRegistrationIncomingMatchFeatureInputSetFedProperties(
    String exchangeName,
    String queueName,
    String deadLetterQueueName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String deadLetterExchangeName,
    Integer batchSize,
    Duration batchReceiveTimeout
) {}
