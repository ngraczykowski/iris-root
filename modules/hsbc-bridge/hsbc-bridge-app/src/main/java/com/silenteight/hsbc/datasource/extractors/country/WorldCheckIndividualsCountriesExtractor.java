package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsCountriesExtractor {

  private final WorldCheckIndividual worldCheckIndividual;

  public Stream<String> extract() {
    return Stream.of(
        worldCheckIndividual.getCountryOfBirth(),
        worldCheckIndividual.getNationalities(),
        worldCheckIndividual.getPassportCountry(),
        worldCheckIndividual.getNativeAliasLanguageCountry()
    ).filter(StringUtils::isNotBlank);
  }
}
