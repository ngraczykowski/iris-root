package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerIndividualsAddressesExtractor {

  private final MatchData matchData;

  List<List<String>> extract() {
    return Optional.ofNullable(matchData.getCustomerIndividuals())
        .map(individuals ->
            individuals.stream()
                .map(individual -> of(individual.getAddress(), individual.getProfileFullAddress()).map(GeoLocationExtractor::stripAndUpper).collect(toList()))
                .collect(toList()))
        .orElse(Collections.emptyList());
  }
}
