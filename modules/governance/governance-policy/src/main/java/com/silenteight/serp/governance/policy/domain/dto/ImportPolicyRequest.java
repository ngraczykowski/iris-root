package com.silenteight.serp.governance.policy.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.StepType;

import java.util.Collection;
import java.util.List;

@Value
@Builder
public class ImportPolicyRequest {

  @NonNull
  String policyName;

  @NonNull
  String createdBy;

  @NonNull
  List<StepConfiguration> stepConfigurations;

  @Value
  @Builder
  public static class StepConfiguration {

    @NonNull
    FeatureVectorSolution solution;

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
    Condition condition;

    @NonNull
    Collection<String> values;
  }
}
