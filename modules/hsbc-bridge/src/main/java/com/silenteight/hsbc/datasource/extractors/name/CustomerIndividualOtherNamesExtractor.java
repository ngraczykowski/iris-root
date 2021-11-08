package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualOtherNamesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    return NameExtractor.collectNames(Stream.of(
        customerIndividual.getFullNameDerived(),
        customerIndividual.getOriginalScriptName()));
  }
}
