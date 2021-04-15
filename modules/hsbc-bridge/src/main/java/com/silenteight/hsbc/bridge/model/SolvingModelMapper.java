package com.silenteight.hsbc.bridge.model;

import com.silenteight.model.api.v1.SolvingModel;

import java.util.stream.Collectors;

class SolvingModelMapper {

  static SolvingModelDto mapToSolvingModelDto(SolvingModel solvingModel) {
    return SolvingModelDto.builder()
        .name(solvingModel.getName())
        .policyName(solvingModel.getPolicyName())
        .strategyName(solvingModel.getStrategyName())
        .features(solvingModel.getFeaturesList().stream()
            .map(feature -> FeatureDto.builder()
                .name(feature.getName())
                .agentConfig(feature.getAgentConfig())
                .build())
            .collect(Collectors.toList()))
        .categories(solvingModel.getCategoriesList())
        .build();
  }
}
