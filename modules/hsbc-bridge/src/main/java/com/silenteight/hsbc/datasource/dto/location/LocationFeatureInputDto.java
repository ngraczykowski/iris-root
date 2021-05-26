package com.silenteight.hsbc.datasource.dto.location;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class LocationFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyLocations = emptyList();
  @Builder.Default
  List<String> watchlistLocations = emptyList();
}
