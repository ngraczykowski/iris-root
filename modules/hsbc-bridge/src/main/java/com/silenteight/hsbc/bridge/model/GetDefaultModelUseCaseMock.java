package com.silenteight.hsbc.bridge.model;

import java.util.List;

class GetDefaultModelUseCaseMock implements ModelUseCase {

  @Override
  public SolvingModelDto getSolvingModel() {
    return SolvingModelDto.builder()
        .name("name")
        .policyName("policyName")
        .strategyName("strategyName")
        .features(List.of(FeatureDto.builder()
            .name("feature_1")
            .agentConfig("agentConfig_1")
            .build()))
        .categories(List.of("category_1"))
        .build();
  }
}
