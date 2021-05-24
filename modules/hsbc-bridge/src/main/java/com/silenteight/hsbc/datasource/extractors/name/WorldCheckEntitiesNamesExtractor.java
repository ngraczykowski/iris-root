package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.extractNameAndOriginalScriptAliases;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class WorldCheckEntitiesNamesExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  public Stream<String> extract() {
    var names = worldCheckEntities.stream()
        .flatMap(WorldCheckEntitiesNamesExtractor::extractWorldCheckEntitiesNames);
    return collectNames(names);
  }

  private static Stream<String> extractWorldCheckEntitiesNames(WorldCheckEntity worldCheckEntity) {
    var streamEntityNameOriginal =
        extractNameAndOriginalScriptAliases(worldCheckEntity.getEntityNameOriginal());

    var streamEntityNames =
        of(
            worldCheckEntity.getEntityNameDerived(),
            worldCheckEntity.getOriginalScriptName());

    return concat(streamEntityNameOriginal, streamEntityNames);
  }
}
