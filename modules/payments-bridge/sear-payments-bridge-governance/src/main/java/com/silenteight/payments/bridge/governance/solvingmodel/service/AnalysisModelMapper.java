package com.silenteight.payments.bridge.governance.solvingmodel.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel.Feature;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AnalysisModelMapper {

  static AnalysisModel fromSolvingModel(SolvingModel model) {
    return AnalysisModel.builder()
        .name(model.getName())
        .policyName(model.getPolicyName())
        .strategyName(model.getStrategyName())
        .features(model
            .getFeaturesList()
            .stream()
            .map(f -> Feature
                .builder()
                .name(f.getName())
                .agentConfig(f.getAgentConfig())
                .build())
            .collect(
                toList()))
        .categories(model.getCategoriesList())
        .build();
  }
}
