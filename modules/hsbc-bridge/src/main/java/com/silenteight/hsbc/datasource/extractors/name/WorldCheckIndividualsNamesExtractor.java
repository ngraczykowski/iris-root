package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsNamesExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public Stream<String> extract() {
    var primaryNames = new ArrayList<String>();
    var fullNamesOriginal = new ArrayList<String>();
    var fullNamesDerived = new ArrayList<String>();

    worldCheckIndividuals.forEach(e -> {
      primaryNames.add(e.getPrimaryName());
      fullNamesOriginal.add(e.getFullNameOriginal());
      fullNamesDerived.add(e.getFullNameDerived());
    });

    var names = Stream.of(
            primaryNames,
            fullNamesOriginal,
            fullNamesDerived)
        .flatMap(Collection::stream);
    return NameExtractor.collectNames(names);
  }

  public Stream<String> extractOtherNames() {
    var names = worldCheckIndividuals.stream()
        .flatMap(worldCheckIndividual -> Stream.of(
            NameExtractor.joinNameParts(
                worldCheckIndividual.getGivenNamesOriginal(),
                worldCheckIndividual.getFamilyNameOriginal()),
            worldCheckIndividual.getOriginalScriptName()
        ));

    return NameExtractor.collectNames(names);
  }
}
