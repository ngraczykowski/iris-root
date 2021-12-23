package com.silenteight.payments.bridge.svb.newlearning.job.historical;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.svb-learning.historical-risk-assessment")
public class HistoricalRiskAssessmentJobProperties {

  private int chunkSize = 1;

}
