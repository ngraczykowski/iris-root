package com.silenteight.serp.governance.policy.featurevector.dto;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureVectorDto {

  @NonNull
  private Map<String, String> featureValues;
}
