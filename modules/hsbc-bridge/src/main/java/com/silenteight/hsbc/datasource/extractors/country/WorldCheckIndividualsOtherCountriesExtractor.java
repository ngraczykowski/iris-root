package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

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
    return of(
        worldCheckIndividual.getCountryCodesAll(),
        worldCheckIndividual.getCountriesAll(),
        worldCheckIndividual.getCountriesOriginal()
    );
  }
}
