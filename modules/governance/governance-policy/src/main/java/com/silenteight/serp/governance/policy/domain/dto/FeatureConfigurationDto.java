package com.silenteight.serp.governance.policy.domain.dto;

import lombok.*;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureConfigurationDto {

  @NonNull
  private String name;
  @NonNull
  private Collection<String> values;
}
