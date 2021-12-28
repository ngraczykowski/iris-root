package com.silenteight.payments.bridge.app.metrics.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("pb.metrics.alert-resolution")
class AlertResolutionTimeMetricsConfiguration {

  private double[] percentiles = new double[] {};
  private boolean histogram = false;
}
