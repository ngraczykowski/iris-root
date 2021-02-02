package com.silenteight.serp.governance.policy.featurevector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class FeatureVectorWithUsageDto {

  @NonNull
  String signature;
  long usageCount;
  @NonNull
  List<String> names;
  @NonNull
  List<String> values;
}
