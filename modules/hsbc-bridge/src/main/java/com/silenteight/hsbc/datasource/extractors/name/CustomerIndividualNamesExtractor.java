package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualNamesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    var joinedName = NameExtractor.joinNameParts(
        customerIndividual.getGivenName(),
        customerIndividual.getMiddleName(),
        customerIndividual.getFamilyNameOriginal());
    return NameExtractor.collectNames(Stream.of(joinedName));
  }
}
