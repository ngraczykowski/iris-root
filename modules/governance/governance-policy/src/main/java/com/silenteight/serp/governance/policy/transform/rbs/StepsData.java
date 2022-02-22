package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.*;

import com.silenteight.serp.governance.policy.domain.dto.Solution;

import java.util.List;

@Value
@Builder
class StepsData {

  List<Step> steps;

  @NonNull
  String name;

  @Value
  @Builder
  public static class Step {

    @NonNull
    Solution solution;
    String reasoningBranchId;
    List<Feature> features;
  }

  @Value
  public static class Feature {

    String name;
    String value;
  }
}
