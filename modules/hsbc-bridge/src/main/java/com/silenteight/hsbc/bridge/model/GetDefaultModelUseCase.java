package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import com.google.protobuf.Empty;

@RequiredArgsConstructor
public class GetDefaultModelUseCase {

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;

  public SolvingModelDto getSolvingModel() {
    SolvingModel defaultSolvingModel =
        solvingModelServiceBlockingStub.getDefaultSolvingModel(Empty.getDefaultInstance());

    return SolvingModelDto.builder()
        .name(defaultSolvingModel.getName())
        .policyName(defaultSolvingModel.getPolicyName())
        .strategyName(defaultSolvingModel.getStrategyName())
        .agentConfigs(defaultSolvingModel.getAgentConfigsList())
        .build();
  }
}
