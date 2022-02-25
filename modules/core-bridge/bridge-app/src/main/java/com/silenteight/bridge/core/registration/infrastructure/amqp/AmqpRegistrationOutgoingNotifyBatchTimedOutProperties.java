package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("amqp.registration.outgoing.notify-batch-timed-out")
public record AmqpRegistrationOutgoingNotifyBatchTimedOutProperties(String exchangeName) {}
