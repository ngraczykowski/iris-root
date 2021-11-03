package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class PrivateListIndividualsAddressExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  List<List<String>> extract() {
    return privateListIndividuals.stream()
        .map(privateListIndividual -> of(privateListIndividual.getAddress())
            .map(GeoLocationExtractor::stripAndUpper)
            .collect(toList()))
        .collect(toList());
  }

}
