package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListEntitiesNamesExtractor {

  private final List<PrivateListEntity> privateListEntities;

  public Stream<String> extract() {
    var names = privateListEntities.stream()
        .flatMap(PrivateListEntitiesNamesExtractor::extractPrivateListEntityNames);
    return NameExtractor.collectNames(names);
  }

  private static Stream<String> extractPrivateListEntityNames(PrivateListEntity privateListEntity) {
    return Stream.of(
        NameExtractor.joinNameParts(
            privateListEntity.getEntityNameOriginal(),
            privateListEntity.getEntityNameDerived())
    );
  }
}
