/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.notifier;

import lombok.RequiredArgsConstructor;

import com.silenteight.backend.solutiondiscrepancy.SolutionDiscrepancyProperties;
import com.silenteight.sep.base.common.time.LocalWithTimeZoneDateFormatter;

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
