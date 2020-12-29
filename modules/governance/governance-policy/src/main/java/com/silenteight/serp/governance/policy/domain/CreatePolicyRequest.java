package com.silenteight.serp.governance.policy.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;

import java.util.Collection;
import java.util.UUID;

@Value
@Builder
public class CreatePolicyRequest {

  @NonNull
  UUID policyId;

  @NonNull
  String policyName;

  @NonNull
  String createdBy;

  @NonNull
  Collection<StepConfiguration> stepConfigurations;

  @Value
  @Builder
  public static class StepConfiguration {

    @NonNull
    FeatureVectorSolution solution;

    @NonNull
    UUID stepId;

    @NonNull
    String stepName;

    String stepDescription;

    @NonNull
    StepType stepType;

    @NonNull
    Collection<FeatureLogicConfiguration> featureLogicConfigurations;
  }

  @Value
  @Builder
  public static class FeatureLogicConfiguration {

    int count;

    @NonNull
    Collection<FeatureConfiguration> featureConfigurations;
  }

  @Value
  @Builder
  public static class FeatureConfiguration {

    @NonNull
    String name;

    @NonNull
    Collection<String> values;
  }
}
