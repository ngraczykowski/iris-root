package com.silenteight.scb.feeding.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.feeding.outgoing.match-feature-input-set-fed")
public record AmqpFeedingOutgoingMatchFeatureInputSetFedProperties(String exchangeName) {}
