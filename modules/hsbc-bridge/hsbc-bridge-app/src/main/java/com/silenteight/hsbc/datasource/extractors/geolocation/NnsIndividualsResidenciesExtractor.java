/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class NnsIndividualsResidenciesExtractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  public List<String> extract() {
    return nnsIndividuals.stream()
        .flatMap(NnsIndividualsResidenciesExtractor::extractWorldCheckFields)
        .collect(Collectors.toList());
  }

  private static Stream<String> extractWorldCheckFields(
      NegativeNewsScreeningIndividuals nnsIndividual) {
    return Stream.of(nnsIndividual.getAddress());
  }
}
