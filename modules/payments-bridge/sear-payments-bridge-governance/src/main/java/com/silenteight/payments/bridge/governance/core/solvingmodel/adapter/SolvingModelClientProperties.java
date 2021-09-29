package com.silenteight.payments.bridge.governance.core.solvingmodel.adapter;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties("pb.grpc.client.solvingmodel")
@Data
@Validated
class SolvingModelClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

  private Duration timeout = DEFAULT_TIMEOUT;
}
