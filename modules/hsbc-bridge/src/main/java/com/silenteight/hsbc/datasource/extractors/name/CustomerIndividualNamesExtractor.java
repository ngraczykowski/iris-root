package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.joinNameParts;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerIndividualNamesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    var joinedName = joinNameParts(
        customerIndividual.getGivenName(),
        customerIndividual.getMiddleName(),
        customerIndividual.getFamilyNameOriginal());
    return collectNames(of(joinedName));
  }
}
