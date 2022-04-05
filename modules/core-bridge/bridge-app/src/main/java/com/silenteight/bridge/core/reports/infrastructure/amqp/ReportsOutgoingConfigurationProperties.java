package com.silenteight.bridge.core.reports.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.reports.outgoing")
public record ReportsOutgoingConfigurationProperties(
    String exchangeName,
    String routingKey
) {}
