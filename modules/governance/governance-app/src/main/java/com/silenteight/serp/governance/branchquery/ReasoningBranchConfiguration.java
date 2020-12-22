package com.silenteight.serp.governance.branchquery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReasoningBranchConfiguration {

  @Bean
  ReasoningBranchFinder reasoningBranchFinder(
      ReasoningBranchQueryRepository branchQueryRepository) {
    return new ReasoningBranchFinder(branchQueryRepository);
  }
}
