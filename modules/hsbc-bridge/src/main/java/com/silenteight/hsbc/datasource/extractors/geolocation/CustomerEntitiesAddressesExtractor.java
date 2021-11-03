package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerEntitiesAddressesExtractor {

  private final MatchData matchData;

  List<List<String>> extract() {
    return Optional.ofNullable(matchData.getCustomerEntities())
        .map(entities ->
            entities.stream()
                .map(ent -> of(ent.getAddress(), ent.getProfileFullAddress()).map(GeoLocationExtractor::stripAndUpper).collect(toList()))
                .collect(toList()))
        .orElse(Collections.emptyList());
  }
}
