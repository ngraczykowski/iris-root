/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class NnsEntitiesOtherCountriesExtractor {

  private final List<NegativeNewsScreeningEntities> nnsEntities;

  public Stream<String> extract() {
    return nnsEntities.stream()
        .flatMap(NnsEntitiesOtherCountriesExtractor::extractWorldCheckEntitiesOtherCountries);
  }

  private static Stream<String> extractWorldCheckEntitiesOtherCountries(
      NegativeNewsScreeningEntities nnsEntity) {
    return Stream.of(
            nnsEntity.getAddressCountry(),
            nnsEntity.getOperatingCountries(),
            nnsEntity.getAllCountryCodes(),
            nnsEntity.getAllCountries(),
            nnsEntity.getNativeAliasLanguageCountry())
        .filter(StringUtils::isNotBlank);
  }
}
