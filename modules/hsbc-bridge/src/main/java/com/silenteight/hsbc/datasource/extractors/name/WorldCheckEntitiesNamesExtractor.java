package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckEntitiesNamesExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  public Stream<String> extract() {
    var names = worldCheckEntities.stream()
        .flatMap(WorldCheckEntitiesNamesExtractor::extractWorldCheckEntitiesNames);
    return NameExtractor.collectNames(names);
  }

  private static Stream<String> extractWorldCheckEntitiesNames(WorldCheckEntity worldCheckEntity) {
    var streamEntityNameOriginal =
        NameExtractor.extractNameAndOriginalScriptAliases(worldCheckEntity.getEntityNameOriginal());

    var streamEntityNames =
        Stream.of(
            worldCheckEntity.getEntityNameDerived(),
            worldCheckEntity.getOriginalScriptName());

    return Stream.concat(streamEntityNameOriginal, streamEntityNames);
  }
}
