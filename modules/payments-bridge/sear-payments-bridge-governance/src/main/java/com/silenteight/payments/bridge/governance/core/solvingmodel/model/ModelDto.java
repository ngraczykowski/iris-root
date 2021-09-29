package com.silenteight.payments.bridge.governance.core.solvingmodel.model;

import lombok.*;

import com.silenteight.model.api.v1.SolvingModel;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ModelDto {

  private String name;
  private String policyName;
  private String strategyName;
  private List<Feature> features;
  private List<String> categories;

  public static ModelDto fromSolvingModel(SolvingModel model) {
    return ModelDto.builder()
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

  @Value
  @Builder
  public static class Feature {

    String name;
    String agentConfig;
  }
}

