package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.governance.api.v1.FeatureVectorSolution;

import java.util.Collection;
import java.util.UUID;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepConfigurationDto {

  @NonNull
  private UUID id;
  @NonNull
  private FeatureVectorSolution solution;
  @NonNull
  @Default
  private Collection<FeatureLogicConfigurationDto> featureLogics = of();
}
