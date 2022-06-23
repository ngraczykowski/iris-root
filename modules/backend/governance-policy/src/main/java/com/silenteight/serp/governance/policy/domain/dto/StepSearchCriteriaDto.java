package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class StepSearchCriteriaDto {

  @NonNull
  List<ConditionSearchCriteriaDto> conditions;

  @Value
  public static class ConditionSearchCriteriaDto {

    @NonNull
    String name;

    @NonNull
    List<String> values;
  }
}
