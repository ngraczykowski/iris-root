package com.silenteight.payments.bridge.governance.solvingmodel.model;

import lombok.*;

import com.silenteight.model.api.v1.SolvingModel;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AnalysisModel {

  private String name;
  private String policyName;
  private String strategyName;
  private List<Feature> features;
  private List<String> categories;

  @Value
  @Builder
  public static class Feature {

    String name;
    String agentConfig;
  }

  public static AnalysisModel fromSolvingModel(SolvingModel model) {
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
