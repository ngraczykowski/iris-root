package com.silenteight.serp.governance.policy.featurevector.dto;

import lombok.*;
import lombok.Builder.Default;

import java.util.Collection;

import static java.util.List.of;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureVectorsColumnsDto {

  @NonNull
  @Default
  private Collection<String> columns = of();
}
