/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.solutiondiscrepancy;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.backend.branch.BranchService;

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
