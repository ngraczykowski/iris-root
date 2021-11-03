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
class CustomerIndividualsCountriesExtractor {

  private final MatchData matchData;
  private final CountryDiscoverer countryDiscoverer;

  List<List<String>> extract() {
    var countriesFromIndividuals = Optional.ofNullable(matchData.getCustomerIndividuals())
        .map(individuals ->
            individuals.stream()
                .map(individual -> of(individual.getResidenceCountries(), individual.getAddressCountry()).map(GeoLocationExtractor::stripAndUpper).collect(toList()))
                .collect(toList()))
        .orElse(Collections.emptyList());
    return countryDiscoverer.discover(countriesFromIndividuals);
  }
}
