package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsOtherCountriesExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public Stream<String> extract() {
    return worldCheckIndividuals
        .stream()
        .flatMap(
            WorldCheckIndividualsOtherCountriesExtractor::extractWorldCheckIndividualsOtherCountries);
  }

  private static Stream<String> extractWorldCheckIndividualsOtherCountries(
      WorldCheckIndividual worldCheckIndividual) {
    return Stream.of(
        worldCheckIndividual.getCountryCodesAll(),
        worldCheckIndividual.getCountriesAll(),
        worldCheckIndividual.getCountriesOriginal()
    ).filter(StringUtils::isNotBlank);
  }
}
