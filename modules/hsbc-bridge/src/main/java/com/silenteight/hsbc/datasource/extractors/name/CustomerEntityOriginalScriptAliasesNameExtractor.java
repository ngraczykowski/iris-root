package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.extractNameAndOriginalScriptAliases;

@RequiredArgsConstructor
class CustomerEntityOriginalScriptAliasesNameExtractor {

  private final String entityNameOriginal;

  public Stream<String> extract() {
    var names = extractNameAndOriginalScriptAliases(entityNameOriginal);
    return collectNames(names);
  }
}
