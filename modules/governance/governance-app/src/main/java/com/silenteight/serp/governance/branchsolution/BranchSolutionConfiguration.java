package com.silenteight.serp.governance.branchsolution;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BranchSolutionsProperties.class)
class BranchSolutionConfiguration {

  @Bean
  BranchSolutionUseCase branchSolutionsUseCase(BranchSolutionsProperties properties) {
    return new BranchSolutionUseCase(properties.isHintedEnabled());
  }
}
