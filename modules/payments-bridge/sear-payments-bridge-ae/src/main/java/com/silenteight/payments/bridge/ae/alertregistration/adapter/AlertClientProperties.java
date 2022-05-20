package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties("pb.grpc.client.alert")
@Data
@Validated
class AlertClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  private Duration timeout = DEFAULT_TIMEOUT;
}
