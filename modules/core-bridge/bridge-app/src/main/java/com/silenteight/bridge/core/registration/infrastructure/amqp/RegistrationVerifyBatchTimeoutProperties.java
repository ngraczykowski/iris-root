package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties(prefix = "registration.verify-batch-timeout")
public record RegistrationVerifyBatchTimeoutProperties(
    boolean enabled,
    Duration delayTime
) {}
