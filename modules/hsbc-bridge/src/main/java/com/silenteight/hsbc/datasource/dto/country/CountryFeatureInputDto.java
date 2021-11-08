package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class CountryFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyCountries = Collections.emptyList();
  @Builder.Default
  List<String> watchlistCountries = Collections.emptyList();
}
