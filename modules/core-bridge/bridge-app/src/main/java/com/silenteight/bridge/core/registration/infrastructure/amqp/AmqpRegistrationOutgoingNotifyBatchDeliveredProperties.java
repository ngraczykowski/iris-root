package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.registration.outgoing.notify-batch-delivered")
public record AmqpRegistrationOutgoingNotifyBatchDeliveredProperties(String exchangeName) {}
