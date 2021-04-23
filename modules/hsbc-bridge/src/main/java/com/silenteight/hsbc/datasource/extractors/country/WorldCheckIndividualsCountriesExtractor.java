package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals;

import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsCountriesExtractor {

  private final WorldCheckIndividuals worldCheckIndividuals;

  public Stream<String> extract() {
    return Stream.of(
        worldCheckIndividuals.getCountryOfBirth(),
        worldCheckIndividuals.getNationalities(),
        worldCheckIndividuals.getPassportCountry(),
        worldCheckIndividuals.getNativeAliasLanguageCountry()
    );
  }
}
