package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualOtherCountriesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    return Stream.of(
        customerIndividual.getEdqAddressCountryCode(),
        customerIndividual.getCountriesAll(),
        customerIndividual.getEdqCountriesAllCodes()
    ).filter(StringUtils::isNotBlank);
  }
}
