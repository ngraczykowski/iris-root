package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerEntityOtherNamesExtractor {

  private final CustomerEntity customerEntity;

  public Stream<String> extract() {
    return NameExtractor.collectNames(Stream.of(
        customerEntity.getEntityName(),
        customerEntity.getOriginalScriptName()));
  }
}
