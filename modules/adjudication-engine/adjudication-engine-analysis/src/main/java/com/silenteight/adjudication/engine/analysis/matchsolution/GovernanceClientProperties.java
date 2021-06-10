package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "ae.grpc.client.governance")
@Validated
@Data
public class GovernanceClientProperties {

  private static final Duration TIMEOUT_DEFAULT = Duration.ofSeconds(5);

  private Duration timeout = TIMEOUT_DEFAULT;
}
