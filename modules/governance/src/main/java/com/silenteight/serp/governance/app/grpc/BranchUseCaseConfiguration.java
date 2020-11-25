package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.branchquery.ReasoningBranchFinder;
import com.silenteight.serp.governance.branchquery.VectorSolutionFinder;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BranchUseCaseConfiguration {

  private final ReasoningBranchFinder reasoningBranchFinder;
  private final FeatureVectorFinder featureVectorFinder;
  private final VectorSolutionFinder vectorSolutionFinder;
  private final DecisionGroupService decisionGroupService;

  @Bean
  ListReasoningBranchesUseCase listReasoningBranchesUseCase() {
    return new ListReasoningBranchesUseCase(reasoningBranchFinder);
  }

  @Bean
  GetReasoningBranchUseCase getReasoningBranchUseCase() {
    return new GetReasoningBranchUseCase(reasoningBranchFinder);
  }

  @Bean
  GetReasoningBranchIdCollectionUseCase getReasoningBranchIdCollectionUseCase() {
    return new GetReasoningBranchIdCollectionUseCase(featureVectorFinder);
  }

  @Bean
  GetVectorSolutionsUseCase getVectorSolutionsUseCase() {
    return new GetVectorSolutionsUseCase(vectorSolutionFinder, decisionGroupService);
  }
}
