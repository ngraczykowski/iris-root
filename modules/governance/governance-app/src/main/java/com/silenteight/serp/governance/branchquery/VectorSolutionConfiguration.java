package com.silenteight.serp.governance.branchquery;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class VectorSolutionConfiguration {

  private final VectorSolutionQueryRepository solutionQueryRepository;

  @Bean
  VectorSolutionFinder vectorSolutionFinder() {
    return new VectorSolutionFinder(solutionQueryRepository);
  }
}
