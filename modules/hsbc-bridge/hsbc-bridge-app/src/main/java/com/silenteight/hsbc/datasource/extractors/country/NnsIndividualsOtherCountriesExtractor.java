/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class NnsIndividualsOtherCountriesExtractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  public Stream<String> extract() {
    return nnsIndividuals
        .stream()
        .flatMap(
            NnsIndividualsOtherCountriesExtractor::extractNnsIndividualsOtherCountries);
  }

  private static Stream<String> extractNnsIndividualsOtherCountries(
      NegativeNewsScreeningIndividuals worldCheckIndividual) {
    return Stream.of(
        worldCheckIndividual.getAllCountryCodes(),
        worldCheckIndividual.getAllCountries(),
        worldCheckIndividual.getOriginalCountries()
    ).filter(StringUtils::isNotBlank);
  }
}
