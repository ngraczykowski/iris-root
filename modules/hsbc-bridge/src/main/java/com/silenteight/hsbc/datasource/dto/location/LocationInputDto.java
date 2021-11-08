package com.silenteight.hsbc.datasource.dto.location;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class LocationInputDto {

  String match;
  @Builder.Default
  List<LocationFeatureInputDto> featureInputs = Collections.emptyList();
}
