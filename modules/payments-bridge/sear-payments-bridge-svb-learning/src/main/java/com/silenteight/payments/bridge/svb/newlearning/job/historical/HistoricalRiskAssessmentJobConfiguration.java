package com.silenteight.payments.bridge.svb.newlearning.job.historical;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
class HistoricalRiskAssessmentJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

}
