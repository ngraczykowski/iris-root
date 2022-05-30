package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.registration.outgoing.data-retention")
public record AmqpRegistrationOutgoingDataRetentionProperties(
    String exchangeName,
    String alertsExpiredRoutingKey) {}
