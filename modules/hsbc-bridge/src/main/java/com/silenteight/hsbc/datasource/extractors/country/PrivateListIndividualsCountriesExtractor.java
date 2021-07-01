package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsCountriesExtractor {

  private final PrivateListIndividual privateListIndividual;

  public Stream<String> extract() {
    return Stream.of(
        privateListIndividual.getCountryOfBirth(),
        privateListIndividual.getNationalities()
    );
  }
}
