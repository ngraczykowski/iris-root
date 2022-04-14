package com.silenteight.qco.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp.report")
public record ReportAmqpConfigurationProperties(
    String exchangeName,
    String routingKey
) {}
