package com.silenteight.serp.governance.policy.featurevector.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.List;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureVectorDto {

  @NonNull
  private String signature;
  long usageCount;
  @NonNull
  @Default
  private List<String> values = of();
}
