package com.silenteight.payments.bridge.svb.learning.job.remove;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.sear-learning.remove")
public class RemoveFileDataJobProperties {

  private int chunkSize = 10;
}
