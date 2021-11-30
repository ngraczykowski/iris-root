
package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.svb-learning")
class StoreCsvFileProperties {

  private int chunkSize = 10;
  private String fileEncoding = "CP1250";

}
