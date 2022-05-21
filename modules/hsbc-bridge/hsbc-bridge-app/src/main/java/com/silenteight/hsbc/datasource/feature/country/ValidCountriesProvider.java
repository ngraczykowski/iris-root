package com.silenteight.hsbc.datasource.feature.country;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidCountriesProvider {

  private final CountryValidator countryValidator;

  public ValidCountriesProvider() {
    this.countryValidator = new CountryValidator();
  }

  public List<String> validateAndMapCountries(List<String> countries) {
    var validCountries = countries.stream()
        .flatMap(this::validateAndMapCountry)
        .collect(Collectors.toSet());

    return List.copyOf(validCountries);
  }

  private Stream<String> validateAndMapCountry(String country) {
    List<String> possibleCountries = Arrays.asList(country.split(" "));
    if (isCountryValid(country) || containsInvalidCountry(possibleCountries)) {
      return Stream.of(country);
    } else {
      return possibleCountries.stream();
    }

  }

  private boolean containsInvalidCountry(List<String> countries) {
    return countries.stream()
        .anyMatch(Predicate.not(this::isCountryValid));
  }

  private boolean isCountryValid(String country) {
    return countryValidator.validate(country);
  }
}
