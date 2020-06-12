package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.report;

import lombok.AllArgsConstructor;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.IsoOffsetDateFormatter;

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
