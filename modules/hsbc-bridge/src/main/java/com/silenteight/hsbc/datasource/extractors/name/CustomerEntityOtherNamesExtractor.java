package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerEntityOtherNamesExtractor {

  private final CustomerEntity customerEntity;

  public Stream<String> extract() {
    return collectNames(of(
        customerEntity.getEntityName(),
        customerEntity.getOriginalScriptName()));
  }
}
