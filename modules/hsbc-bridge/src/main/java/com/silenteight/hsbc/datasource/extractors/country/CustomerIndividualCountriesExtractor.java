package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualCountriesExtractor {

  private final CustomerIndividual customerIndividual;

  public Stream<String> extract() {
    return Stream.of(
        customerIndividual.getNationalityCitizenshipCountries(),
        customerIndividual.getNationalityOrCitizenship(),
        customerIndividual.getNationalityCountries(),
        customerIndividual.getPassportIssueCountry(),
        customerIndividual.getCountryOfBirthOriginal(),
        customerIndividual.getCountryOfBirth(),
        customerIndividual.getEdqBirthCountryCode()
    ).filter(StringUtils::isNotBlank);
  }
}
