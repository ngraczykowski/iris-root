package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.model.FeatureDto;
import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;

import java.util.List;

class ModelServiceClientMock implements ModelServiceClient {

  @Override
  public SolvingModelDto getSolvingModel() {
    return SolvingModelDto.builder()
        .name("name")
        .policyName("policy")
        .strategyName("strategy")
        .features(List.of(FeatureDto.builder()
            .name("feature")
            .agentConfig("agentConfig")
            .build()))
        .categories(List.of("category"))
        .build();
  }
}
