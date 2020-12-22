package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeSummaryFinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class DecisionTreeUseCaseConfiguration {

  private final DecisionTreeSummaryFinder decisionTreeFinder;

  @Bean
  GetDecisionTreeUseCase getDecisionTreeGrpcUseCase() {
    return new GetDecisionTreeUseCase(decisionTreeFinder);
  }

  @Bean
  ListDecisionTreesUseCase listDecisionTreesGrpcUseCase() {
    return new ListDecisionTreesUseCase(decisionTreeFinder);
  }
}
