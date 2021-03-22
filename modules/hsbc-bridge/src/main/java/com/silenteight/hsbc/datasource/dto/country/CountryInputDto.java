package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CountryInputDto {

  String match;
  List<CountryFeatureInputDto> countryFeatureInputs;
}
