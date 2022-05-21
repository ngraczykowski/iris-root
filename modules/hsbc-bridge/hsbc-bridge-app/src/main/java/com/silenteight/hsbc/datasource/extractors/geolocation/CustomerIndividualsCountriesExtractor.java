package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualsCountriesExtractor {

  private final MatchData matchData;
  private final CountryDiscoverer countryDiscoverer;

  List<List<String>> extract() {
    var countriesFromIndividuals = Optional.ofNullable(matchData.getCustomerIndividuals())
        .map(individuals ->
            individuals.stream()
                .map(individual -> Stream.of(individual.getResidenceCountries(), individual.getAddressCountry())
                    .map(GeoLocationExtractor::stripAndUpper)
                    .collect(Collectors.toList()))
                .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
    return countryDiscoverer.discover(countriesFromIndividuals);
  }
}
