package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsNamesExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public Stream<String> extract() {
    var names = privateListIndividuals.stream()
        .flatMap(PrivateListIndividualsNamesExtractor::extractPrivateListIndividualNames);
    return NameExtractor.collectNames(names);
  }

  private static Stream<String> extractPrivateListIndividualNames(
      PrivateListIndividual privateListIndividual) {
    return Stream.of(
        privateListIndividual.getFullNameOriginal(),
        NameExtractor.joinNameParts(
            privateListIndividual.getGivenNamesOriginal(),
            privateListIndividual.getFamilyNameOriginal()),
        privateListIndividual.getFullNameDerived()
    );
  }
}
