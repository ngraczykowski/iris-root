package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.joinNameParts;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class PrivateListEntitiesNamesExtractor {

  private final List<PrivateListEntity> privateListEntities;

  public Stream<String> extract() {
    var names = privateListEntities.stream()
        .flatMap(PrivateListEntitiesNamesExtractor::extractPrivateListEntityNames);
    return collectNames(names);
  }

  private static Stream<String> extractPrivateListEntityNames(PrivateListEntity privateListEntity) {
    return of(
        joinNameParts(
            privateListEntity.getEntityNameOriginal(),
            privateListEntity.getEntityNameDerived())
    );
  }
}
