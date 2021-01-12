package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.StepType;

import java.util.Collection;
import java.util.UUID;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDto {

  @NonNull
  private UUID id;
  @NonNull
  private String name;
  @NonNull
  private StepType type;
  private String description;
  @NonNull
  private FeatureVectorSolution solution;
  @NonNull
  @Default
  private Collection<FeatureLogicDto> featureLogics = of();
}
