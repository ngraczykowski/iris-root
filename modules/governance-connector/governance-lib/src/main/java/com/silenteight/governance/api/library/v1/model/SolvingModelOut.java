package com.silenteight.governance.api.library.v1.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.model.api.v1.SolvingModel;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
public class SolvingModelOut {

  String name;
  String policyName;
  String strategyName;
  List<FeatureOut> features;
  List<String> categories;

  static SolvingModelOut createFrom(SolvingModel solvingModel) {
    return SolvingModelOut.builder()
        .name(solvingModel.getName())
        .policyName(solvingModel.getPolicyName())
        .strategyName(solvingModel.getStrategyName())
        .features(collectFeatures(solvingModel))
        .categories(solvingModel.getCategoriesList())
        .build();
  }

  private static List<FeatureOut> collectFeatures(SolvingModel solvingModel) {
    return solvingModel.getFeaturesList().stream()
        .map(FeatureOut::createFrom)
        .collect(Collectors.toList());
  }
}
