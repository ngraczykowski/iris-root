package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CountryFeatureInputDto {

  String feature;
  List<String> alertedPartyCountries;
  List<String> watchlistCountries;
}
