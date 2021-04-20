package com.silenteight.hsbc.datasource.feature.converter.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.CustomerIndividuals;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class CustomerIndividualCountriesExtractor {

  private final CustomerIndividuals customerIndividuals;

  public Stream<String> extract() {
    return Stream.of(
        customerIndividuals.getNationalityCitizenshipCountries(),
        customerIndividuals.getNationalityOrCitizenship(),
        customerIndividuals.getNationalityCountries(),
        customerIndividuals.getPassportIssueCountry(),
        customerIndividuals.getCountryOfBirthOriginal(),
        customerIndividuals.getCountryOfBirth(),
        customerIndividuals.getEdqBirthCountryCode()
    );
  }
}
