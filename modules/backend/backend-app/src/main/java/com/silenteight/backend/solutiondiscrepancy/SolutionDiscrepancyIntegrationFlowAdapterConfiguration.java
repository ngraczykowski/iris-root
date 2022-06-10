/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.solutiondiscrepancy;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class SolutionDiscrepancyIntegrationFlowAdapterConfiguration {

  private final SolutionDiscrepancyHandler solutionDiscrepancyHandler;

  @Bean
  SolutionDiscrepancyIntegrationFlowAdapter solutionDiscrepancyIntegrationFlowAdapter() {
    return new SolutionDiscrepancyIntegrationFlowAdapter(solutionDiscrepancyHandler);
  }
}
