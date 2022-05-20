package com.silenteight.hsbc.datasource.dto.location;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LocationFeatureInputDto {

  String feature;
  @Builder.Default
  String alertedPartyLocation = "";
  @Builder.Default
  String watchlistLocation = "";
}
