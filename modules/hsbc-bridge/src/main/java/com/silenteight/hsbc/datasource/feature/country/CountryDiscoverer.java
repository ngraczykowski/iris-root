package com.silenteight.hsbc.datasource.feature.country;


import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountryDiscoverer {

  private final MatchNormalizer matchNormalizer = new MatchNormalizer();
  private final DisplayNormalizer displayNormalizer = new DisplayNormalizer();
  private final CountryValidator countryValidator = new CountryValidator();

  public List<List<String>> discover(List<List<String>> countries) {
    return countries.stream()
        .map(this::discoverForSingleCustomer)
        .collect(Collectors.toList());
  }

  @NotNull
  private List<String> discoverForSingleCustomer(List<String> countriesForSingleCustomer) {
    return countriesForSingleCustomer
        .stream()
        .filter(Objects::nonNull)
        .flatMap(country -> discoverSingleCountry(country)
            .orElseGet(() -> splitAndDiscoverMultipleCountries(country)))
        .distinct()
        .collect(Collectors.toList());
  }

  private Stream<String> splitAndDiscoverMultipleCountries(String country) {
    return Stream.of(Splitter.values())
        .map(splitter -> splitter.split(country))
        .filter(splitterResult -> splitterResult.allMatch(matchNormalizer::normalize, countryValidator::validate))
        .findFirst()
        .map(splittingResult -> splittingResult.map(displayNormalizer::normalize))
        .orElse(Stream.of(country.strip()));
  }

  private Optional<Stream<String>> discoverSingleCountry(String country) {
    var normalizedSingleCountry = matchNormalizer.normalize(country);

    if (StringUtils.isEmpty(normalizedSingleCountry)) {
      return Optional.of(Stream.empty());
    }

    if (countryValidator.validate(normalizedSingleCountry)) {
      return Optional.of(Stream.of(displayNormalizer.normalize(country)));
    }
    return Optional.empty();
  }
}
