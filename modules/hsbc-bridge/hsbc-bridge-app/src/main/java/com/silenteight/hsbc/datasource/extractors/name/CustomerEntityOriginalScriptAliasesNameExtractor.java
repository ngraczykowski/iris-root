package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerEntityOriginalScriptAliasesNameExtractor {

  private final String entityNameOriginal;

  public Stream<String> extract() {
    var names = NameExtractor.extractNameAndOriginalScriptAliases(entityNameOriginal);
    return NameExtractor.collectNames(names);
  }
}
