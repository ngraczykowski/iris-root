package com.silenteight.sens.webapp.backend.reasoningbranch.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListReasoningBranchesConfiguration {

  @Bean
  DummyReasoningBranchesQuery dummyReasoningBranchesQuery() {
    return new DummyReasoningBranchesQuery();
  }
}
