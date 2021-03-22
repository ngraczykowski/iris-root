package com.silenteight.hsbc.datasource.dto.location;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class LocationFeatureInputDto {

  String feature;
  List<String> alertedPartyLocations;
  List<String> watchlistLocations;
}
