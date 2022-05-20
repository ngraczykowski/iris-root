package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualsAddressesExtractor {

  private final MatchData matchData;

  List<List<String>> extract() {
    return Optional.ofNullable(matchData.getCustomerIndividuals())
        .map(individuals ->
            individuals.stream()
                .map(individual -> Stream.of(individual.getAddress(), individual.getProfileFullAddress())
                    .map(GeoLocationExtractor::stripAndUpper)
                    .collect(Collectors.toList()))
                .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
