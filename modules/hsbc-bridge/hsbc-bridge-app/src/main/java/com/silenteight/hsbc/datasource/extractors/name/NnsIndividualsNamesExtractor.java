/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class NnsIndividualsNamesExtractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  public Stream<String> extract() {
    var primaryNames = new ArrayList<String>();
    var fullNamesOriginal = new ArrayList<String>();
    var fullNamesDerived = new ArrayList<String>();

    nnsIndividuals.forEach(
        e -> {
          primaryNames.add(e.getPrimaryName());
          fullNamesOriginal.add(e.getOriginalFullName());
          fullNamesDerived.add(e.getDerivedFullName());
        });

    var names =
        Stream.of(primaryNames, fullNamesOriginal, fullNamesDerived).flatMap(Collection::stream);
    return NameExtractor.collectNames(names);
  }

  public Stream<String> extractOtherNames() {
    var names =
        nnsIndividuals.stream()
            .flatMap(
                nnsIndividual ->
                    Stream.of(
                        NameExtractor.joinNameParts(
                            nnsIndividual.getOriginalGivenNames(),
                            nnsIndividual.getOriginalFamilyName()),
                        nnsIndividual.getOriginalScriptName()));

    return NameExtractor.collectNames(names);
  }
}
