package com.silenteight.payments.bridge.governance.core.solvingmodel.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Model {

  private String name;
  private String policyName;
  private String strategyName;
  private List<Feature> features;
  private List<String> categories;

  @Value
  public class Feature {

    String name;
    String agentConfig;
  }
}

