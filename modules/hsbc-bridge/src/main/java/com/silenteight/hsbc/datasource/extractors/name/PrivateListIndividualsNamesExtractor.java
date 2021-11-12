package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsNamesExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public Stream<String> extract() {
    var primaryNames = new ArrayList<String>();
    var fullNamesOriginal = new ArrayList<String>();
    var fullNamesDerived = new ArrayList<String>();

    privateListIndividuals.forEach(e -> {
      primaryNames.add(e.getPrimaryName());
      fullNamesOriginal.add(e.getFullNameOriginal());
      fullNamesDerived.add(e.getFullNameDerived());
    });

    var names = Stream.of(
            primaryNames,
            fullNamesOriginal,
            fullNamesDerived)
        .flatMap(Collection::stream);
    return NameExtractor.collectNames(names);
  }

  public Stream<String> extractOtherNames() {
    var names = privateListIndividuals.stream()
        .flatMap(privateListIndividual -> Stream.of(
            NameExtractor.joinNameParts(
                privateListIndividual.getGivenNamesOriginal(),
                privateListIndividual.getFamilyNameOriginal())
        ));

    return NameExtractor.collectNames(names);
  }
}
