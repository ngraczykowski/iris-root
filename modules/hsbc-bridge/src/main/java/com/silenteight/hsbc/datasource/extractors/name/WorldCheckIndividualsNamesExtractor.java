package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsNamesExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public Stream<String> extract() {
    var names = worldCheckIndividuals.stream()
        .flatMap(WorldCheckIndividualsNamesExtractor::extractWorldCheckNames);
    return NameExtractor.collectNames(names);
  }

  private static Stream<String> extractWorldCheckNames(WorldCheckIndividual worldCheckIndividual) {
    return Stream.of(
        worldCheckIndividual.getFullNameOriginal(),
        worldCheckIndividual.getFullNameDerived(),
        worldCheckIndividual.getOriginalScriptName(),
        NameExtractor.joinNameParts(
            worldCheckIndividual.getGivenNamesOriginal(),
            worldCheckIndividual.getFamilyNameOriginal())
    );
  }
}
