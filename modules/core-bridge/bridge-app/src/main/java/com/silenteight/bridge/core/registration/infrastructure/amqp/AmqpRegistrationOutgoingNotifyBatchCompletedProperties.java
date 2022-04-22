package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.registration.outgoing.notify-batch-completed")
public record AmqpRegistrationOutgoingNotifyBatchCompletedProperties(
    String exchangeName,
    String solvingBatchRoutingKey,
    String simulationBatchRoutingKey
) {}
