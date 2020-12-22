package com.silenteight.serp.governance.solutiondiscrepancy;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.serp.governance.branch.BranchService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SolutionDiscrepancyProperties.class)
class SolutionDiscrepancyConfiguration {

  private final BranchService branchService;
  private final SolutionDiscrepancyProperties properties;

  @Bean
  SolutionDiscrepancyHandler solutionDiscrepancyHandler() {
    return new SolutionDiscrepancyHandler(
        branchService,
        properties.isDryRunEnabled(),
        DefaultTimeSource.INSTANCE);
  }
}
