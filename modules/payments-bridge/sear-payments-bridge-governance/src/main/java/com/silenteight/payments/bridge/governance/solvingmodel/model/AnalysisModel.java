package com.silenteight.payments.bridge.governance.solvingmodel.model;

import lombok.*;

import java.util.List;

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
}
