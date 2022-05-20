package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerEntitiesCountriesExtractor {

  private final MatchData matchData;
  private final CountryDiscoverer countryDiscoverer;

  List<List<String>> extract() {
    return countryDiscoverer.discover(
        matchData.getCustomerEntities().stream()
            .map(entity -> Stream.of(entity.getAddressCountry())
                .map(GeoLocationExtractor::stripAndUpper)
                .collect(Collectors.toList()))
            .collect(Collectors.toList()));
  }
}
