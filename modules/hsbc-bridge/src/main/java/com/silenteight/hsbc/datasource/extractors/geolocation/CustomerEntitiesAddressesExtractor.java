package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerEntitiesAddressesExtractor {

  private final MatchData matchData;

  List<List<String>> extract() {
    return Optional.ofNullable(matchData.getCustomerEntities())
        .map(entities ->
            entities.stream()
                .map(ent -> Stream
                    .of(ent.getAddress(), ent.getProfileFullAddress()).map(GeoLocationExtractor::stripAndUpper).collect(
                        Collectors.toList()))
                .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
