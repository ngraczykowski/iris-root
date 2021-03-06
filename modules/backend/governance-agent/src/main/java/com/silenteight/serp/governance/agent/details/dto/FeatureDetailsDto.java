package com.silenteight.serp.governance.agent.details.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class FeatureDetailsDto {

  @NonNull
  String name;
  @NonNull
  String displayName;
}
