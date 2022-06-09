/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;
import com.silenteight.hsbc.datasource.extractors.common.SignType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class NnsIndividualsPlaceOfBirthExtractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  public List<String> extract() {
    return nnsIndividuals.stream()
        .flatMap(NnsIndividualsPlaceOfBirthExtractor::extractNnsFields)
        .collect(Collectors.toList());
  }

  private static Stream<String> extractNnsFields(
      NegativeNewsScreeningIndividuals nnsIndividual) {
    return GeoLocationExtractor.splitExtractedValueBySign(
        SignType.SEMICOLON, nnsIndividual.getOriginalPlaceOfBirth());
  }
}
