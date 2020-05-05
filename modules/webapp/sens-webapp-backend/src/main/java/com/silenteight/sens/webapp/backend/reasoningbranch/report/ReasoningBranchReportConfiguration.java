package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;
import com.silenteight.sens.webapp.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sens.webapp.common.time.IsoOffsetDateFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class ReasoningBranchReportConfiguration {

  @Bean
  ReasoningBranchReportGenerator reasoningBranchReportGenerator(
      ReasoningBranchesReportQuery reasoningBranchesByTreeQuery, FeatureQuery featureQuery) {
    return new ReasoningBranchReportGenerator(
        reasoningBranchesByTreeQuery,
        featureQuery,
        DefaultTimeSource.INSTANCE,
        DigitsOnlyDateFormatter.INSTANCE,
        IsoOffsetDateFormatter.INSTANCE);
  }
}
