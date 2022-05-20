package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;
import com.silenteight.hsbc.datasource.extractors.common.SignType;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualCountriesExtractor {

  private static final String SPECIAL_CHARACTERS_PATTERN = "[\\\"]";
  private final List<CustomerIndividual> customerIndividuals;

  public Stream<String> extract() {
    var separatedFields = customerIndividuals.stream()
        .flatMap(customerIndividual -> Stream.of(
            customerIndividual.getNationalityOrCitizenship(),
            customerIndividual.getNationalityCountries()))
        .filter(StringUtils::isNotBlank)
        .flatMap(this::separateStrings)
        .map(this::removeSpecialCharacters)
        .map(String::strip);

    var fields = customerIndividuals.stream()
        .flatMap(customerIndividual -> Stream.of(
            customerIndividual.getNationalityCitizenshipCountries(),
            customerIndividual.getPassportIssueCountry(),
            customerIndividual.getCountryOfBirthOriginal(),
            customerIndividual.getCountryOfBirth(),
            customerIndividual.getEdqBirthCountryCode()));

    return Stream.concat(separatedFields, fields)
        .filter(StringUtils::isNotBlank);
  }

  private Stream<String> separateStrings(String field) {
    return Stream.of(field.split(
        SignType.COMMA.getSign() + SignType.PIPE.getSign() + SignType.SEMICOLON.getSign()));
  }

  private String removeSpecialCharacters(String field) {
    return field.replaceAll(SPECIAL_CHARACTERS_PATTERN, "");
  }
}
