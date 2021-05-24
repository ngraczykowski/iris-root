package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.joinNameParts;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class PrivateListIndividualsNamesExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public Stream<String> extract() {
    var names = privateListIndividuals.stream()
        .flatMap(PrivateListIndividualsNamesExtractor::extractPrivateListIndividualNames);
    return collectNames(names);
  }

  private static Stream<String> extractPrivateListIndividualNames(
      PrivateListIndividual privateListIndividual) {
    return of(
        privateListIndividual.getFullNameOriginal(),
        joinNameParts(
            privateListIndividual.getGivenNamesOriginal(),
            privateListIndividual.getFamilyNameOriginal()),
        privateListIndividual.getFullNameDerived()
    );
  }
}
