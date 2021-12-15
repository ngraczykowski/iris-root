package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = "pb.svb-learning.unregistered")
class UnregisteredJobProperties {

  private int chunkSize = 10;
  private long retryPeriodMilliseconds = 30 * 1000;
  private int retryLimit = 5;
}
