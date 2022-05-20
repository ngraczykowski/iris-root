package com.silenteight.adjudication.engine.governance;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties("ae.grpc.client.alert-solving")
@Data
@Validated
class AlertSolvingClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

  private Duration timeout = DEFAULT_TIMEOUT;
}
