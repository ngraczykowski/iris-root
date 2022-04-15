package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.Collection;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturesLogicDto {

  @NonNull
  @Default
  private Collection<FeatureLogicDto> featuresLogic = of();
}
