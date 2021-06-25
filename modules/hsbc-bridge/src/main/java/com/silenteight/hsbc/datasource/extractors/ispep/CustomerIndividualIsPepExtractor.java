package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualIsPepExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    return Stream.of(
        customerIndividual.getLobCountry()
    );
  }
}
