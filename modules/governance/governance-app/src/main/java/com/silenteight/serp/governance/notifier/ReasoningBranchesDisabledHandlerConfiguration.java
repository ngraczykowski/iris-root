package com.silenteight.serp.governance.notifier;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.LocalWithTimeZoneDateFormatter;
import com.silenteight.serp.governance.solutiondiscrepancy.SolutionDiscrepancyProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SolutionDiscrepancyProperties.class)
class ReasoningBranchesDisabledHandlerConfiguration {

  private final SolutionDiscrepancyProperties properties;

  @Bean
  ReasoningBranchesDisabledHandler reasoningBranchesDisabledHandler() {
    return new ReasoningBranchesDisabledHandler(
        properties.isDryRunEnabled(), LocalWithTimeZoneDateFormatter.INSTANCE);
  }
}
