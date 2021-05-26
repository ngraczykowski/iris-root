package com.silenteight.hsbc.datasource.dto.location;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class LocationInputDto {

  String match;
  @Builder.Default
  List<LocationFeatureInputDto> featureInputs = emptyList();
}
