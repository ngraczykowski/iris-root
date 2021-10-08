package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerIndividualIsPepExtractor {

  private final List<CustomerIndividual> customerIndividuals;

  public Stream<String> extract() {
    return customerIndividuals.stream()
        .flatMap(customerIndividual -> of(customerIndividual.getEdqLobCountryCode()));
  }
}
