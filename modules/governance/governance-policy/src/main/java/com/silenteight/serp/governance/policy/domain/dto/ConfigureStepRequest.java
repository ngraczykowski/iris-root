package com.silenteight.serp.governance.policy.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Collection;

@Value
@Builder
public class ConfigureStepRequest {

  long stepId;

  @NonNull
  Collection<FeatureLogicConfiguration> featureLogicConfigurations;

  @Value
  @Builder
  public static class FeatureLogicConfiguration {

    int count;

    Collection<FeatureConfiguration> featureConfigurations;
  }

  @Value
  @Builder
  public static class FeatureConfiguration {

    String name;

    Collection<String> values;
  }
}
