/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class NnsEntitiesNamesExtractor {

  private final List<NegativeNewsScreeningEntities> nnsEntities;

  public Stream<String> extract() {
    var primaryNames = new ArrayList<String>();
    var entityNamesOriginal = new ArrayList<String>();
    var entityNamesDerived = new ArrayList<String>();

    nnsEntities.forEach(e -> {
      primaryNames.add(e.getPrimaryName());
      entityNamesOriginal.addAll(extractEntityOriginalName(e.getOriginalEntityName()));
      entityNamesDerived.add(e.getDerivedEntityName());
    });

    var names = Stream.of(
            primaryNames,
            entityNamesOriginal,
            entityNamesDerived)
        .flatMap(Collection::stream);
    return NameExtractor.collectNames(names);
  }

  public Stream<String> extractOtherNames() {
    var names = nnsEntities.stream()
        .flatMap(this::extractNnsEntitiesOtherNames);

    return NameExtractor.collectNames(names);
  }

  private Stream<String> extractNnsEntitiesOtherNames(NegativeNewsScreeningEntities worldCheckEntity) {
    return Stream.of(worldCheckEntity.getOriginalScriptName());
  }

  private List<String> extractEntityOriginalName(String name) {
    return NameExtractor.extractNameAndOriginalScriptAliases(name)
        .collect(Collectors.toList());
  }
}
