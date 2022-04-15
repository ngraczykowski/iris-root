package com.silenteight.serp.governance.branch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BranchConfiguration {

  @Bean
  BranchService branchService() {
    return new BranchService();
  }
}
