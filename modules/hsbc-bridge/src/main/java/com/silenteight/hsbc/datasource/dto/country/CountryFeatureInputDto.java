package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class CountryFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyCountries = emptyList();
  @Builder.Default
  List<String> watchlistCountries = emptyList();
}
