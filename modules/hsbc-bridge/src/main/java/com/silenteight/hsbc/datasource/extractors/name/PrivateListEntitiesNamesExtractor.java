package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListEntitiesNamesExtractor {

  private final List<PrivateListEntity> privateListEntities;

  public Stream<String> extract() {
    var primaryNames = new ArrayList<String>();
    var entityNamesOriginal = new ArrayList<String>();
    var entityNamesDerived = new ArrayList<String>();

    privateListEntities.forEach(e -> {
      primaryNames.add(e.getPrimaryName());
      entityNamesOriginal.add(e.getEntityNameOriginal());
      entityNamesDerived.add(e.getEntityNameDerived());
    });

    var names = Stream.of(
            primaryNames,
            entityNamesOriginal,
            entityNamesDerived)
        .flatMap(Collection::stream);
    return NameExtractor.collectNames(names);
  }
}
