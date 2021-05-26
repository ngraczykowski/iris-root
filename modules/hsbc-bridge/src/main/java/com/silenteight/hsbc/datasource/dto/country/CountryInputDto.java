package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class CountryInputDto {

  String match;

  @Builder.Default
  List<CountryFeatureInputDto> featureInputs = emptyList();
}
