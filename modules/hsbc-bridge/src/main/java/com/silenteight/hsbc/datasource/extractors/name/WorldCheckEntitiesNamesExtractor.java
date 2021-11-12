package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckEntitiesNamesExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  public Stream<String> extract() {
    var primaryNames = new ArrayList<String>();
    var entityNamesOriginal = new ArrayList<String>();
    var entityNamesDerived = new ArrayList<String>();

    worldCheckEntities.forEach(e -> {
      primaryNames.add(e.getPrimaryName());
      entityNamesOriginal.addAll(extractEntityOriginalName(e.getEntityNameOriginal()));
      entityNamesDerived.add(e.getEntityNameDerived());
    });

    var names = Stream.of(
            primaryNames,
            entityNamesOriginal,
            entityNamesDerived)
        .flatMap(Collection::stream);
    return NameExtractor.collectNames(names);
  }

  public Stream<String> extractOtherNames() {
    var names = worldCheckEntities.stream()
        .flatMap(this::extractWorldCheckEntitiesOtherNames);

    return NameExtractor.collectNames(names);
  }

  private Stream<String> extractWorldCheckEntitiesOtherNames(WorldCheckEntity worldCheckEntity) {
    return Stream.of(worldCheckEntity.getOriginalScriptName());
  }

  private List<String> extractEntityOriginalName(String name) {
    return NameExtractor.extractNameAndOriginalScriptAliases(name)
        .collect(Collectors.toList());
  }
}
