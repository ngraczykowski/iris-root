package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualEdqLobCountryCodeExtractor {

  private final List<CustomerIndividual> customerIndividuals;

  public Stream<String> extract() {
    return customerIndividuals.stream().map(CustomerIndividual::getEdqLobCountryCode);
  }
}
