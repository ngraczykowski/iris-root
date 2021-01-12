package com.silenteight.serp.governance.policy.solve;

import lombok.*;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FeatureLogic {

  private int count;

  @NonNull
  private Collection<Feature> features;
}
