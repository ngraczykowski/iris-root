package com.silenteight.serp.governance.model.agent;

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
  List<FeatureDto> agents;
}
