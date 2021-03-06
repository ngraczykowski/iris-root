package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.registration.incoming.recommendation-stored")
record AmqpRegistrationIncomingRecommendationStoredProperties(
    String queueName,
    String deadLetterQueueName,
    Integer deadLetterQueueTimeToLiveInMilliseconds,
    String deadLetterExchangeName
) {}
