
package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = "pb.svb-learning.store")
public class StoreCsvJobProperties {

  private long retryPeriodMilliseconds = 30 * 1000;
  private boolean skipDeletion;
  private int retryLimit = 5;

}
