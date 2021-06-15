package com.silenteight.serp.governance.agent.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class FeaturesListDto {

  @NonNull
  List<FeatureDto> features;
}
