package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class CountryInputDto {

  String match;

  @Builder.Default
  List<CountryFeatureInputDto> featureInputs = Collections.emptyList();
}
