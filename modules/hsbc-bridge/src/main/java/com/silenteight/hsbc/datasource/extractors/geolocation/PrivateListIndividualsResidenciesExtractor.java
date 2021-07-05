package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class PrivateListIndividualsResidenciesExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public List<String> extract() {
    return privateListIndividuals.stream()
        .flatMap(PrivateListIndividualsResidenciesExtractor::extractPrivateListFields)
        .collect(toList());
  }

  private static Stream<String> extractPrivateListFields(
      PrivateListIndividual privateListIndividual) {
    return of(privateListIndividual.getAddress());
  }
}
