package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerIndividualOtherCountriesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    return of(
        customerIndividual.getEdqAddressCountryCode(),
        customerIndividual.getCountriesAll(),
        customerIndividual.getEdqCountriesAllCodes()
    ).filter(StringUtils::isNotBlank);
  }
}
