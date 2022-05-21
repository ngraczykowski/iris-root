package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsCountryExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  List<List<String>> extract() {
    return privateListIndividuals.stream()
        .map(individual -> Stream.of(individual.getAddressCountry())
            .map(GeoLocationExtractor::stripAndUpper)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

}
