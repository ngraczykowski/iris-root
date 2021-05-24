package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.extractNameAndOriginalScriptAliases;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.removeUnnecessaryAsterisk;

@RequiredArgsConstructor
class CustomerIndividualOriginalScriptAliasesNameExtractor {

  private final String profileFullName;

  public Stream<String> extract() {
    var name = removeUnnecessaryAsterisk(profileFullName);
    var names = extractNameAndOriginalScriptAliases(name);
    return collectNames(names);
  }
}
