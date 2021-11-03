package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class PrivateListIndividualsCountryExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  List<List<String>> extract() {
    return privateListIndividuals.stream()
        .map(individual -> of(individual.getAddressCountry())
            .map(GeoLocationExtractor::stripAndUpper)
            .collect(toList()))
        .collect(toList());
  }

}
