package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
class CustomerIndividualEdqLobCountryCodeExtractor {

  private final List<CustomerIndividual> customerIndividuals;

  public String extract() {
    return customerIndividuals.stream()
        .map(CustomerIndividual::getEdqLobCountryCode)
        .filter(StringUtils::isNotBlank)
        .findFirst()
        .orElse("");
  }
}
