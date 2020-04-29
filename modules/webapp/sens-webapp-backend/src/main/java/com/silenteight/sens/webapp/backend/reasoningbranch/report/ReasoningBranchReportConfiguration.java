package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.report.DigitsOnlyDateFormater;
import com.silenteight.sens.webapp.backend.report.IsoOffsetDateFormatter;
import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

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
        DigitsOnlyDateFormater.INSTANCE,
        IsoOffsetDateFormatter.INSTANCE);
  }
}
