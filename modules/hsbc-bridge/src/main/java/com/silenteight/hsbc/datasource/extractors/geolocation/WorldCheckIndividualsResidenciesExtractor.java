package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class WorldCheckIndividualsResidenciesExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public List<String> extract() {
    return worldCheckIndividuals.stream()
        .flatMap(WorldCheckIndividualsResidenciesExtractor::extractWorldCheckFields)
        .collect(toList());
  }

  private static Stream<String> extractWorldCheckFields(WorldCheckIndividual worldCheckIndividual) {
    return of(worldCheckIndividual.getAddress());
  }
}
