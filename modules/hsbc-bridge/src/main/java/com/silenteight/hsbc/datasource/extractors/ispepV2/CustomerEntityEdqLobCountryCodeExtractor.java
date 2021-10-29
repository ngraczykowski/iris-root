package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
class CustomerEntityEdqLobCountryCodeExtractor {

  private final List<CustomerEntity> customerEntities;

  public String extract() {
    return customerEntities.stream()
        .map(CustomerEntity::getEdqLobCountryCode)
        .filter(StringUtils::isNotBlank)
        .findFirst()
        .orElse("");
  }
}
