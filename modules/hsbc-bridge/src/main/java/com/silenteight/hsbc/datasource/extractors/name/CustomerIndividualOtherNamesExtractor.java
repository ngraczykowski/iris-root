package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerIndividualOtherNamesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    return collectNames(of(
        customerIndividual.getFullNameDerived(),
        customerIndividual.getOriginalScriptName()));
  }
}
