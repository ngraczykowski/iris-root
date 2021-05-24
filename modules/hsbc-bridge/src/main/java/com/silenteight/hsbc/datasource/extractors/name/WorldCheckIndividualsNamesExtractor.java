package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.joinNameParts;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class WorldCheckIndividualsNamesExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public Stream<String> extract() {
    var names = worldCheckIndividuals.stream()
        .flatMap(WorldCheckIndividualsNamesExtractor::extractWorldCheckNames);
    return collectNames(names);
  }

  private static Stream<String> extractWorldCheckNames(WorldCheckIndividual worldCheckIndividual) {
    return of(
        worldCheckIndividual.getFullNameOriginal(),
        worldCheckIndividual.getFullNameDerived(),
        worldCheckIndividual.getOriginalScriptName(),
        joinNameParts(
            worldCheckIndividual.getGivenNamesOriginal(),
            worldCheckIndividual.getFullNameOriginal())
    );
  }
}
