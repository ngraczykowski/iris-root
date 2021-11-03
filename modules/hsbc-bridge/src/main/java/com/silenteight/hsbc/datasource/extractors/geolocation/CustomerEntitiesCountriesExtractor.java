package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerEntitiesCountriesExtractor {

  private final MatchData matchData;
  private final CountryDiscoverer countryDiscoverer;

  List<List<String>> extract() {
    return countryDiscoverer.discover(
        matchData.getCustomerEntities().stream()
            .map(entity -> of(entity.getAddressCountry()).map(GeoLocationExtractor::stripAndUpper).collect(toList()))
            .collect(toList()));
  }
}
