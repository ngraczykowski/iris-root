package com.silenteight.universaldatasource.api.library.country.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.country.v1.CountryInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class CountryInputOut {

  String match;

  @Builder.Default
  List<CountryFeatureInputOut> countryFeatureInputs = List.of();

  static CountryInputOut createFrom(CountryInput input) {
    return CountryInputOut.builder()
        .match(input.getMatch())
        .countryFeatureInputs(input.getCountryFeatureInputsList()
            .stream()
            .map(CountryFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
