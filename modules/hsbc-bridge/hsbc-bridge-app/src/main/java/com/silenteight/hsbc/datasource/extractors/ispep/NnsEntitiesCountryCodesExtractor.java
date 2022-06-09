/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class NnsEntitiesCountryCodesExtractor {

  private final List<NegativeNewsScreeningEntities> nnsEntities;

  Stream<String> extract() {
    return nnsEntities.stream()
        .map(NegativeNewsScreeningEntities::getAllCountryCodes)
        .filter(StringUtils::isNotBlank)
        .flatMap(CountryCodesSplitter::splitCountryCodesBySpace)
        .filter(StringUtils::isNotBlank)
        .map(String::strip);
  }
}
