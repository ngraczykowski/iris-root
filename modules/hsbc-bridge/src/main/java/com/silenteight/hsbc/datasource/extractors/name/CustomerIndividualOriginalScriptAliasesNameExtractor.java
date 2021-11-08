package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualOriginalScriptAliasesNameExtractor {

  private final String profileFullName;

  public Stream<String> extract() {
    var name = NameExtractor.removeUnnecessaryAsterisk(profileFullName);
    var names = NameExtractor.extractNameAndOriginalScriptAliases(name);
    return NameExtractor.collectNames(names);
  }
}
