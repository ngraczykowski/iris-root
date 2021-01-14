package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.Collection;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureLogicConfigurationDto {

  private int count;
  @NonNull
  @Default
  private Collection<FeatureConfigurationDto> features = of();
}
