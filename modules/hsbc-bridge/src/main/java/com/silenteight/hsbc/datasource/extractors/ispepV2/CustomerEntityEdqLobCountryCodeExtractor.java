package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerEntityEdqLobCountryCodeExtractor {

  private final List<CustomerEntity> customerEntities;

  public Stream<String> extract() {
    return customerEntities.stream().map(CustomerEntity::getEdqLobCountryCode);
  }
}
