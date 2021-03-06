package com.silenteight.adjudication.engine.governance;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "ae.grpc.client.policy-steps")
@Validated
@Data
class PolicyStepsClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

  private Duration timeout = DEFAULT_TIMEOUT;
}
