/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class NnsIndividualsCountriesExtractor {

  private final NegativeNewsScreeningIndividuals nnsIndividuals;

  public Stream<String> extract() {
    return Stream.of(
            nnsIndividuals.getCountryOfBirth(),
            nnsIndividuals.getNationalities(),
            nnsIndividuals.getPassportCountry(),
            nnsIndividuals.getNativeAliasLanguageCountry())
        .filter(StringUtils::isNotBlank);
  }
}
